package io.knowledgeassets.myskills.server.skill;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill, String> {
	Optional<Skill> findByName(String name);
}
