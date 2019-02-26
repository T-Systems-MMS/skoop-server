package io.knowledgeassets.myskills.server.community;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.neo4j.transaction.SessionFactoryUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public class RecommendedCommunityRepositoryImpl implements RecommendedCommunityRepository {

	private static final String QUERY = "MATCH (community:Community)-[:RELATES_TO]-(skill:Skill)-[r:RELATED_TO]-(user:User {id:{userId}}) " +
			" WHERE r.priority >= 2 AND NOT (community)-[:HAS_MEMBER]-(user) " +
			" WITH {counter: size(collect(skill)), id: community.id} as res " +
			" MATCH (community: Community) " +
			" WHERE community.id = res.id " +
			" WITH collect(community{community: community, recommended: true, counter: res.counter}) as communities " +
			" MATCH (c:Community) " +
			" WHERE NOT(c.id IN extract(v in communities | v.community.id)) " +
			" WITH c, communities " +
			" OPTIONAL MATCH (c)-[:RELATES_TO]-(skill:Skill)-[:RELATED_TO]-(:User {id:{userId}}) " +
			" WITH c, communities, collect(skill) as skills " +
			" WITH communities + collect({community: c, recommended: false, counter: size(skills)}) AS result " +
			" UNWIND result as r " +
			" OPTIONAL MATCH (:Community {id: r.community.id})-[:MANAGED_BY]->(manager: User) " +
			" WITH collect(properties(manager)) as managers, r " +
			" OPTIONAL MATCH (:Community {id: r.community.id})-[:HAS_MEMBER]->(member: User) " +
			" WITH managers, r, collect(properties(member)) as members " +
			" OPTIONAL MATCH (:Community {id: r.community.id})-[:RELATES_TO]->(skill:Skill) " +
			" WITH r.community as c, r, managers, members, collect(properties(skill)) as skills " +
			" RETURN c{.*, members: members, managers: managers, skills: skills} as community, r.recommended as recommended, r.counter as skillCounter " +
			" ORDER BY r.counter DESC";

	private final SessionFactory sessionFactory;

	private final ObjectMapper objectMapper;

	public RecommendedCommunityRepositoryImpl(SessionFactory sessionFactory, ObjectProvider<ObjectMapper> objectMapperProvider) {
		this.sessionFactory = requireNonNull(sessionFactory);
		this.objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
	}

	@Override
	public Stream<RecommendedCommunity> getRecommendedCommunities(String userId) {
		final Session session = SessionFactoryUtils.getSession(sessionFactory);
		final Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return StreamSupport.stream(session.query(QUERY, params, true).spliterator(), false)
				.map((Map<String, Object> m) -> objectMapper.convertValue(m, RecommendedCommunity.class));
	}
}
