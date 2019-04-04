package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.query.ProjectQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserProjectCommandServiceTests {

	@Mock
	private UserProjectRepository userProjectRepository;

	@Mock
	private ProjectQueryService projectQueryService;

	@Mock
	private UserQueryService userQueryService;

	private UserProjectCommandService userProjectCommandService;

	@BeforeEach
	void setUp() {
		userProjectCommandService = new UserProjectCommandService(userProjectRepository, projectQueryService, userQueryService);
	}

	@Test
	@DisplayName("Assigns project to user")
	void assignsProjectToUser() {
		UserProject userProject = UserProject.builder()
				.role("QA")
				.tasks("testing")
				.user(User.builder()
						.id("123")
						.userName("tester")
						.build())
				.project(Project.builder()
						.id("ABC")
						.name("Project")
						.description("Project description")
						.build())
				.build();
		given(projectQueryService.getProjectById("ABC")).willReturn(Optional.of(Project.builder()
				.id("ABC")
				.name("Project")
				.description("Project description")
				.build()));
		given(userQueryService.getUserById("123")).willReturn(Optional.of(User.builder()
				.id("123")
				.userName("tester")
				.build()));
		given(userProjectRepository.save(ArgumentMatchers.isA(UserProject.class))).willReturn(userProject);
		userProject = userProjectCommandService.assignProjectToUser("ABC", "123", UserProject.builder()
				.role("QA")
				.tasks("testing")
				.user(User.builder()
						.id("123")
						.userName("tester")
						.build())
				.project(Project.builder()
						.id("ABC")
						.name("Project")
						.description("Project description")
						.build())
				.build());
		assertThat(userProject).isNotNull();
		assertThat(userProject.getRole()).isEqualTo("QA");
		assertThat(userProject.getTasks()).isEqualTo("testing");
		assertThat(userProject.getProject()).isNotNull();
		assertThat(userProject.getProject().getName()).isEqualTo("Project");
		assertThat(userProject.getProject().getDescription()).isEqualTo("Project description");
		assertThat(userProject.getUser()).isNotNull();
		assertThat(userProject.getUser().getUserName()).isEqualTo("tester");
	}

	@Test
	@DisplayName("Assign project to user throws an exception when there is no such a project")
	void assignProjectToUserThrowsExceptionWhenThereIsNoSuchProject() {
		given(projectQueryService.getProjectById("ABC")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () ->
				userProjectCommandService.assignProjectToUser("ABC", "123", UserProject.builder()
						.role("QA")
						.tasks("testing")
						.user(User.builder()
								.id("123")
								.userName("tester")
								.build())
						.project(Project.builder()
								.id("ABC")
								.name("Project")
								.description("Project description")
								.build())
						.build()));
	}

	@Test
	@DisplayName("Assign project to user throws an exception when there is no such a user")
	void assignProjectToUserThrowsExceptionWhenThereIsNoSuchUser() {
		given(projectQueryService.getProjectById("ABC")).willReturn(Optional.of(
				Project.builder()
						.id("ABC")
						.name("Project")
						.description("Project description")
						.build()
		));
		given(userQueryService.getUserById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () ->
				userProjectCommandService.assignProjectToUser("ABC", "123", UserProject.builder()
						.role("QA")
						.tasks("testing")
						.user(User.builder()
								.id("123")
								.userName("tester")
								.build())
						.project(Project.builder()
								.id("ABC")
								.name("Project")
								.description("Project description")
								.build())
						.build()));
	}

	@Test
	@DisplayName("Assign project to user throws an exception when there is already a relationship between a specified project and a specified user")
	void assignProjectToUserThrowsExceptionWhenThereIsAlreadyRelationshipBetweenProjectAndUser() {
		given(projectQueryService.getProjectById("ABC")).willReturn(Optional.of(
				Project.builder()
						.id("ABC")
						.name("Project")
						.description("Project description")
						.build()
		));
		given(userQueryService.getUserById("123")).willReturn(Optional.of(
				User.builder()
						.id("123")
						.userName("tester")
						.build()
		));
		given(userProjectRepository.findByUserIdAndProjectId("123", "ABC")).willReturn(Optional.of(UserProject.builder()
				.id(1L)
				.role("developer")
				.tasks("development")
				.build()));
		assertThrows(DuplicateResourceException.class, () ->
				userProjectCommandService.assignProjectToUser("ABC", "123", UserProject.builder()
						.role("QA")
						.tasks("testing")
						.user(User.builder()
								.id("123")
								.userName("tester")
								.build())
						.project(Project.builder()
								.id("ABC")
								.name("Project")
								.description("Project description")
								.build())
						.build()));
	}

	@Test
	@DisplayName("Delete user project throws an exception when there is no such user-project relationship")
	void deleteUserProjectThrowsExceptionWhenThereIsNoSuchUserProjectRelationship() {
		given(userProjectRepository.findByUserIdAndProjectId("123", "ABC")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userProjectCommandService.deleteUserProject("123", "ABC"));
	}

	@Test
	@DisplayName("User project is updated")
	void userProjectIsUpdated() {
		given(userProjectRepository.findByUserIdAndProjectId("123", "ABC")).willReturn(Optional.of(
				UserProject.builder()
						.id(1L)
						.project(Project.builder()
								.id("ABC")
								.name("Project")
								.description("Project description")
								.build())
						.user(User.builder()
								.id("123")
								.userName("tester")
								.build())
						.role("QA")
						.tasks("testing")
						.startDate(LocalDate.of(2019, 1, 11))
						.endDate(LocalDate.of(2019, 5, 11))
						.build()
		));
		given(userProjectRepository.save(ArgumentMatchers.isA(UserProject.class))).willReturn(
			UserProject.builder()
					.role("developer")
					.tasks("development")
					.startDate(LocalDate.of(2019, 1, 10))
					.endDate(LocalDate.of(2019, 5, 10))
					.project(Project.builder()
							.id("ABC")
							.name("Project")
							.description("Project description")
							.build())
					.user(User.builder()
							.id("123")
							.userName("tester")
							.build())
				.build()
		);
		UserProject userProject = userProjectCommandService.updateUserProject("123", "ABC", UpdateUserProjectCommand.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 10))
				.endDate(LocalDate.of(2019, 5, 10))
				.build()
		);
		assertThat(userProject).isNotNull();
		assertThat(userProject.getRole()).isEqualTo("developer");
		assertThat(userProject.getTasks()).isEqualTo("development");
		assertThat(userProject.getStartDate()).isEqualTo(LocalDate.of(2019, 1, 10));
		assertThat(userProject.getEndDate()).isEqualTo(LocalDate.of(2019, 5, 10));
		assertThat(userProject.getUser()).isNotNull();
		assertThat(userProject.getUser().getId()).isEqualTo("123");
		assertThat(userProject.getUser().getUserName()).isEqualTo("tester");
		assertThat(userProject.getProject()).isNotNull();
		assertThat(userProject.getProject().getId()).isEqualTo("ABC");
		assertThat(userProject.getProject().getName()).isEqualTo("Project");
		assertThat(userProject.getProject().getDescription()).isEqualTo("Project description");
	}

	@Test
	@DisplayName("Update user-project relationshop throws an exception when there is no such user-project relationship")
	void updateUserProjectRelationshipThrowsExceptionWhenThereIsNoSuchUserProjectRelationship() {
		given(userProjectRepository.findByUserIdAndProjectId("123", "ABC")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userProjectCommandService.updateUserProject("123", "ABC", UpdateUserProjectCommand.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 10))
				.endDate(LocalDate.of(2019, 5, 10))
				.build()
		));
	}

}
