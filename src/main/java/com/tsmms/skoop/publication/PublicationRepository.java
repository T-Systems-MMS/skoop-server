package com.tsmms.skoop.publication;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface PublicationRepository extends Neo4jRepository<Publication, String> {

	@Query("MATCH (p:Publication)-[:USER]->(:User {id: {userId}}) " +
			" WITH p " +
			" OPTIONAL MATCH (p)-[r:REFERS_TO_SKILL]->(s:Skill) " +
			" RETURN p, r, s ORDER BY p.date DESC")
	Stream<Publication> findByUserIdOrderByDateDesc(String userId);

}
