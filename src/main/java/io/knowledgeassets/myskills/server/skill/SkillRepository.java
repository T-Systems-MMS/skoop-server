package io.knowledgeassets.myskills.server.skill;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends Neo4jRepository<Skill, String> {

	Optional<Skill> findByName(String name);

//	@Query("MATCH (skill:Skill) WHERE skill.name =~ /(?i){search}/ RETURN count(skill) > 0")
	@Query("MATCH (skill:Skill) WHERE TOLOWER(skill.name) = TOLOWER({search}) RETURN count(skill) > 0")
	Boolean findByNameIgnoreCase(@Param("search") String search);
}
