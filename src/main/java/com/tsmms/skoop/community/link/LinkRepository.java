package com.tsmms.skoop.community.link;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends Neo4jRepository<Link, Long> {

}
