package com.tsmms.skoop.project;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends Neo4jRepository<Project, String> {

	@Query("MATCH (project:Project) WHERE TOLOWER(project.name) = TOLOWER({name}) RETURN project")
	Optional<Project> findByNameIgnoreCase(@Param("name") String name);

}
