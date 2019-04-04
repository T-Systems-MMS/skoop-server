package com.tsmms.skoop.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.neo4j.transaction.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Repository
public class AnonymousUserSkillRepositoryImpl implements AnonymousUserSkillRepository {

	private static final String QUERY = "MATCH (user:User)-[userSkill:RELATED_TO]-(skill:Skill) WHERE\n";
	private static final String QUERY_PART = " userSkill.currentLevel >= %s AND skill.id = %s ";
	private static final String QUERY_RESULT = " WITH userSkill, user, skill ORDER BY user.id, skill.id " +
			" WITH collect(userSkill{ .*, skill: properties(skill)}) as userSkills, user, count(userSkill) as skillCount" +
			" WHERE skillCount = $skillCount" +
			" RETURN userSkills, user.referenceId as referenceId";

	private final SessionFactory sessionFactory;

	private final ObjectMapper objectMapper;

	public AnonymousUserSkillRepositoryImpl(SessionFactory sessionFactory, ObjectMapper objectMapper) {
		this.sessionFactory = requireNonNull(sessionFactory);
		this.objectMapper = requireNonNull(objectMapper);
	}

	@Override
	public Stream<AnonymousUserSkillResult> findAnonymousUserSkillsBySkillLevels(List<UserSearchSkillCriterion> searchParams) {
		final Session session = SessionFactoryUtils.getSession(sessionFactory);
		final StringBuilder query = new StringBuilder().append(QUERY);
		final Map<String, Object> params = new HashMap<>();
		for (int i = 0; i < searchParams.size(); i++) {
			final UserSearchSkillCriterion criterion = searchParams.get(i);
			final String levelParam = format("level%d", i);
			final String skillParam = format("skill%d", i);
			query.append(format(QUERY_PART, format("$%s", levelParam), format("$%s", skillParam)));
			params.put(levelParam, criterion.getMinimumCurrentLevel());
			params.put(skillParam, criterion.getSkillId());
			if (i != searchParams.size() - 1) {
				query.append(" OR ");
			}
		}
		query.append(QUERY_RESULT);
		params.put("skillCount", searchParams.size());
		return StreamSupport.stream(session.query(query.toString(), params, true).spliterator(), false)
				.map((Map<String, Object> m) -> objectMapper.convertValue(m, AnonymousUserSkillResult.class));
	}
}
