package com.tsmms.skoop.project.query;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static java.util.stream.Collectors.toList;

@ExtendWith(MockitoExtension.class)
class ProjectQueryServiceTests {

	@Mock
	private ProjectRepository projectRepository;

	private ProjectQueryService projectQueryService;

	@BeforeEach
	void setUp() {
		projectQueryService = new ProjectQueryService(projectRepository);
	}

	@Test
	@DisplayName("Retrieves project by id")
	void retrievesProjectById() {
		given(projectRepository.findById("123")).willReturn(Optional.of(
				Project.builder().id("123").name("Project name").description("Project description").build()
		));
		final Optional<Project> project = projectQueryService.getProjectById("123");
		assertThat(project).isNotEmpty();
		assertThat(project.get().getId()).isEqualTo("123");
		assertThat(project.get().getName()).isEqualTo("Project name");
		assertThat(project.get().getDescription()).isEqualTo("Project description");
	}

	@Test
	@DisplayName("Returns a stream of all projects from the data repository")
	void returnsStreamOfAllSkills() {
		given(projectRepository.findAll()).willReturn(Arrays.asList(
				Project.builder().id("123").name("First project").description("First project description").build(),
				Project.builder().id("345").name("Second project").description("Second project description").build()));

		Stream<Project> projects = projectQueryService.getProjects();

		assertThat(projects).isNotNull();
		List<Project> projectList = projects.collect(toList());
		assertThat(projectList).hasSize(2);
		Project project = projectList.get(0);
		assertThat(project.getId()).isEqualTo("123");
		assertThat(project.getName()).isEqualTo("First project");
		assertThat(project.getDescription()).isEqualTo("First project description");
		project = projectList.get(1);
		assertThat(project.getId()).isEqualTo("345");
		assertThat(project.getName()).isEqualTo("Second project");
		assertThat(project.getDescription()).isEqualTo("Second project description");
	}

}
