package com.tsmms.skoop.testimonial;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends Neo4jRepository<Testimonial, String> {
}
