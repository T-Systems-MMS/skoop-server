package com.tsmms.skoop.testimonial;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface TestimonialRepository extends Neo4jRepository<Testimonial, String> {

	@Query("MATCH (t:Testimonial)-[:USER]->(:User {id: {userId}}) " +
			" WITH t " +
			" OPTIONAL MATCH (t)-[r:REFERS_TO_SKILL]->(s:Skill) " +
			" RETURN t, r, s ORDER BY t.creationDatetime DESC")
	Stream<Testimonial> findByUserIdOrderByCreationDatetimeDesc(String userId);

}
