package com.tsmms.skoop.membership;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MembershipRepository extends Neo4jRepository<Membership, String> {

	@Query("MATCH (m:Membership)-[:USER]->(:User {id: {userId}}) " +
			" WITH m " +
			" OPTIONAL MATCH (m)-[r:REFERS_TO_SKILL]->(s:Skill) " +
			" RETURN m, r, s ORDER BY m.creationDatetime DESC")
	Stream<Membership> findByUserIdOrderByDateDesc(String userId);

}
