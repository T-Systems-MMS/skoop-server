package io.knowledgeassets.myskills.server.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class ProjectRepositoryTests {

	@Autowired
	private ProjectRepository projectRepository;

	@Test
	@DisplayName("Provides the existing project queried by its exact name")
	void providesSkillByExactName() {
		// Given
		projectRepository.save(Project.builder().id("123").name("First project").description("Project description").build());
		// When
		Optional<Project> project = projectRepository.findByNameIgnoreCase("First project");
		// Then
		assertThat(project).isNotEmpty();
		assertThat(project.get().getId()).isEqualTo("123");
		assertThat(project.get().getName()).isEqualTo("First project");
		assertThat(project.get().getDescription()).isEqualTo("Project description");
	}

	@Test
	@DisplayName("Provides the existing project queried by its name ignoring case")
	void providesProjectByNameIgnoringCase() {
		// Given
		projectRepository.save(Project.builder().id("123").name("First project").description("Project description").build());
		// When
		Optional<Project> project = projectRepository.findByNameIgnoreCase("fIrSt prOjeCt");
		// Then
		assertThat(project).isNotEmpty();
		assertThat(project.get().getId()).isEqualTo("123");
		assertThat(project.get().getName()).isEqualTo("First project");
		assertThat(project.get().getDescription()).isEqualTo("Project description");
	}

}
