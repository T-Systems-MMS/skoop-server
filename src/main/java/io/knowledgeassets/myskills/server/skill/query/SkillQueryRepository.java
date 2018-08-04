package io.knowledgeassets.myskills.server.skill.query;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillQueryRepository extends Neo4jRepository<Skill, String> {
	Optional<Skill> findByName(String name);
}
