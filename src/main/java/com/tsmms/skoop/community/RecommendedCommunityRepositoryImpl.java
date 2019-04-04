package com.tsmms.skoop.community;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.neo4j.transaction.SessionFactoryUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public class RecommendedCommunityRepositoryImpl implements RecommendedCommunityRepository {

	private static final String QUERY = "MATCH (community:Community)-[:RELATES_TO]-(skill:Skill)-[r:RELATED_TO]-(user:User {id:{userId}})\n" +
			" WHERE r.priority >= 2 AND NOT (community)-[:COMMUNITY_USER]-(user) " +
			" WITH community, size(collect(skill)) as counter " +
			" OPTIONAL MATCH (:Community {id: community.id})-[r1:COMMUNITY_USER {role:'MANAGER'}]->(manager: User) " +
			" WITH collect(properties(manager)) as managers, community, counter, r1 " +
			" OPTIONAL MATCH (:Community {id: community.id})-[r2:RELATES_TO]->(skill:Skill) " +
			" WITH community, managers, collect(properties(skill)) as skills, counter, r1, r2 " +
			" RETURN community, managers, r1, skills, r2 " +
			" ORDER BY counter DESC";

	private final SessionFactory sessionFactory;

	public RecommendedCommunityRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = requireNonNull(sessionFactory);
	}

	@Override
	public Stream<Community> getRecommendedCommunities(String userId) {
		final Session session = SessionFactoryUtils.getSession(sessionFactory);
		final Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return StreamSupport.stream(session.query(Community.class, QUERY, params).spliterator(), false);
	}
}
