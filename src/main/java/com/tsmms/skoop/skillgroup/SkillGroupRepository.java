package com.tsmms.skoop.skillgroup;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillGroupRepository extends Neo4jRepository<SkillGroup, String> {

	@Query("MATCH (sg:SkillGroup) WHERE TOLOWER(sg.name) = TOLOWER({name}) RETURN sg")
	Optional<SkillGroup> findByNameIgnoreCase(@Param("name") String name);

	@Query("MATCH (sg:SkillGroup) WHERE TOLOWER(sg.name) = TOLOWER({search}) RETURN count(sg) > 0")
	Boolean isSkillGroupExistByNameIgnoreCase(@Param("search") String search);

	@Query("MATCH (sg:SkillGroup) " +
			"WHERE TOLOWER(sg.name) CONTAINS TOLOWER({search}) " +
			"RETURN sg " +
			"ORDER BY sg.name ASC " +
			"LIMIT 20")
	Iterable<SkillGroup> findSkillGroupSuggestions(@Param("search") String search);
}
