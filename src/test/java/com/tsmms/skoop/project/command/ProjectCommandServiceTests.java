package com.tsmms.skoop.project.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.ProjectRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userproject.UserProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjectCommandServiceTests {

	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private NotificationCommandService notificationCommandService;

	private ProjectCommandService projectCommandService;

	@BeforeEach
	void setUp() {
		projectCommandService = new ProjectCommandService(projectRepository, notificationCommandService);
	}

	@Test
	@DisplayName("Tests if project is created.")
	void testIfProjectIsCreated() {
		given(projectRepository.findByNameIgnoreCase("Project")).willReturn(Optional.empty());
		given(projectRepository.save(ArgumentMatchers.isA(Project.class)))
				.willReturn(Project.builder().id("123").name("Project").description("Project description").build());

		Project project = projectCommandService.create(Project.builder().id("123").name("Project").description("Project description").build());

		assertThat(project).isNotNull();
		assertThat(project.getId()).isNotNull();
		assertThat(project.getId()).isEqualTo("123");
		assertThat(project.getName()).isEqualTo("Project");
		assertThat(project.getDescription()).isEqualTo("Project description");
	}

	@Test
	@DisplayName("Create project throws exception when there is duplicate")
	void createProjectThrowsExceptionWhenThereIsDuplicate() {
		given(projectRepository.findByNameIgnoreCase("Project")).willReturn(Optional.of(
				Project.builder().id("123").name("Project").description("Project description").build()));
		assertThrows(DuplicateResourceException.class, () -> {
			projectCommandService.create(Project.builder().id("123").name("Project").description("Project description").build());
		});
	}

	@Test
	@DisplayName("Delete project throws an exception when there is no such a project")
	void deleteProjectThrowsExceptionWhenThereIsNoSuchProject() {
		given(projectRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> projectCommandService.delete("123"));
	}

	@Test
	@DisplayName("Update project throws an exception when there is no such a project")
	void updateProjectThrowsExceptionWhenThereIsNoSuchProject() {
		given(projectRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> projectCommandService.update(UpdateProjectCommand.builder().id("123").name("Project").description("Project description").build()));
	}

	@Test
	@DisplayName("The project is updated")
	void projectIsUpdated() {
		given(projectRepository.findById("123")).willReturn(Optional.of(
				Project.builder()
						.id("123")
						.name("Project name")
						.description("Project description")
						.userProjects(Arrays.asList(UserProject.builder()
								.id("a")
								.role("Software Developer")
								.tasks("Development")
								.startDate(LocalDate.of(2019, 5, 28))
								.endDate(LocalDate.of(2019, 7, 30))
								.creationDate(LocalDateTime.of(2019, 5, 28, 10, 0))
								.lastModifiedDate(LocalDateTime.of(2019, 5, 28, 10, 0))
								.approved(true)
								.user(User.builder()
										.id("1")
										.userName("tester")
										.build())
								.build()))
						.build()
		));
		given(projectRepository.save(ArgumentMatchers.isA(Project.class)))
				.willReturn(Project.builder()
						.id("123")
						.name("New project name")
						.description("New project description")
						.build()
				);

		Project project = projectCommandService.update(UpdateProjectCommand.builder()
				.id("123")
				.name("New project name")
				.description("New project description")
				.build()
		);

		assertThat(project).isNotNull();
		assertThat(project.getId()).isNotNull();
		assertThat(project.getId()).isEqualTo("123");
		assertThat(project.getName()).isEqualTo("New project name");
		assertThat(project.getDescription()).isEqualTo("New project description");
	}

}
