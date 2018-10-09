package io.knowledgeassets.myskills.server.skillgroup;

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
	Boolean isGroupExistByNameIgnoreCase(@Param("search") String search);

	@Query("MATCH (group:SkillGroup) " +
			"WHERE NOT EXISTS((group)-[:RELATED_GROUP]-(:Skill {id:{skillId}})) " +
			"AND TOLOWER(group.name) CONTAINS TOLOWER({search}) " +
			"RETURN group " +
			"ORDER BY group.name ASC " +
			"LIMIT 20")
	Iterable<SkillGroup> findGroupSuggestionsBySkillId(@Param("skillId") String skillId, @Param("search") String search);

	@Query("MATCH (sg:SkillGroup) " +
			"WHERE TOLOWER(sg.name) CONTAINS TOLOWER({search}) " +
			"RETURN sg " +
			"ORDER BY sg.name ASC " +
			"LIMIT 20")
	Iterable<SkillGroup> findGroupSuggestions(@Param("search") String search);
}
