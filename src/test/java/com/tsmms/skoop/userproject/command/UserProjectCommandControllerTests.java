package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.userproject.UserProject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProjectCommandController.class)
class UserProjectCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private SkillQueryService skillQueryService;

	@MockBean
	private UserProjectCommandService userProjectCommandService;

	@MockBean
	private UserQueryService userQueryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Tests if a project can be assigned to a user")
	void testIfProjectCanBeAssignedToUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("user-project/assign-project-to-user.json");
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(skillQueryService.convertSkillNamesToSkillsSet(Collections.singleton("Spring Boot"))).willReturn(Collections.singleton(
				Skill.builder()
						.name("Spring Boot")
						.build()
		));
		given(userProjectCommandService.assignProjectToUser("Project", "1f37fb2a-b4d0-4119-9113-4677beb20ae2", UserProject.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.skills(Collections.singleton(
						Skill.builder()
								.name("Spring Boot")
								.build()
				))
				.build()
		)).willReturn(UserProject.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.id("aaa")
				.creationDate(LocalDateTime.of(2019, 11, 3, 13, 30))
				.lastModifiedDate(LocalDateTime.of(2019, 11, 3, 13, 31))
				.user(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.firstName("test")
						.lastName("testing")
						.email("test@mail.com")
						.build()
				)
				.project(Project.builder()
						.id("123")
						.name("Simple project")
						.industrySector("Software development")
						.customer("Company")
						.creationDate(LocalDateTime.of(2019, 1, 11, 11, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 12, 10, 10))
						.description("Some project description")
						.build()
				)
				.build()
		);
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id", is(equalTo("aaa"))))
					.andExpect(jsonPath("$.role", is(equalTo("developer"))))
					.andExpect(jsonPath("$.tasks", is(equalTo("development"))))
					.andExpect(jsonPath("$.startDate", is(equalTo("2019-01-09"))))
					.andExpect(jsonPath("$.endDate", is(equalTo("2019-05-01"))))
					.andExpect(jsonPath("$.creationDate", is(equalTo("2019-11-03T13:30:00"))))
					.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-11-03T13:31:00"))))
					.andExpect(jsonPath("$.user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.user.firstName", is(equalTo("test"))))
					.andExpect(jsonPath("$.user.lastName", is(equalTo("testing"))))
					.andExpect(jsonPath("$.user.email", is(equalTo("test@mail.com"))))
					.andExpect(jsonPath("$.project.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.project.name", is(equalTo("Simple project"))))
					.andExpect(jsonPath("$.project.industrySector", is(equalTo("Software development"))))
					.andExpect(jsonPath("$.project.customer", is(equalTo("Company"))))
					.andExpect(jsonPath("$.project.description", is(equalTo("Some project description"))))
					.andExpect(jsonPath("$.project.creationDate", is(equalTo("2019-01-11T11:30:00"))))
					.andExpect(jsonPath("$.project.lastModifiedDate", is(equalTo("2019-01-12T10:10:00"))));
		}
	}

	@Test
	@DisplayName("Tests if user project relationship can be deleted.")
	void testIfUserProjectCanBeDeleted() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Tests if user project relationship cannot be deleted by unauthorized user.")
	void testIfUserProjectCannotBeDeletedByUnauthorizedUser() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/1f37cb2a-a5d0-f229-8634-4647beb20ae2/projects/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if user project relationship cannot be deleted by not authenticated user.")
	void testIfUserProjectCannotBeDeletedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(delete("/users/1f37cb2a-a5d0-f229-8634-4647beb20ae2/projects/123"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("User project can be updated by owning user.")
	void userProjectCanBeUpdatedByOwningUser() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.firstName("test")
				.lastName("testing")
				.email("test@mail.com")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("user-project/update-user-project.json");
		given(skillQueryService.convertSkillNamesToSkillsSet(new HashSet<>(Arrays.asList("Spring Boot", "Java")))).willReturn(new HashSet<>(
				Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.name("Java")
								.build()
				)
		));
		given(userProjectCommandService.updateUserProject("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123", UpdateUserProjectCommand.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.name("Java")
										.build()
						)
				))
				.approved(false)
				.build()
		)).willReturn(UserProject.builder()
				.id("bbb")
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.creationDate(LocalDateTime.of(2019, 1, 20, 9, 30))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 11, 0))
				.user(owner)
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("456")
										.name("Java")
										.build()
						)
				))
				.project(Project.builder()
						.id("777")
						.name("Project")
						.customer("Customer")
						.industrySector("Information Technology")
						.creationDate(LocalDateTime.of(2019, 1, 10, 10, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 10, 11, 0))
						.description("Description")
						.build()
				)
				.build()
		);
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(owner));
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123").contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id", is(equalTo("bbb"))))
					.andExpect(jsonPath("$.role", is(equalTo("developer"))))
					.andExpect(jsonPath("$.tasks", is(equalTo("development"))))
					.andExpect(jsonPath("$.startDate", is(equalTo("2019-01-09"))))
					.andExpect(jsonPath("$.endDate", is(equalTo("2019-05-01"))))
					.andExpect(jsonPath("$.creationDate", is(equalTo("2019-01-20T09:30:00"))))
					.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-01-20T11:00:00"))))
					.andExpect(jsonPath("$.user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.user.firstName", is(equalTo("test"))))
					.andExpect(jsonPath("$.user.lastName", is(equalTo("testing"))))
					.andExpect(jsonPath("$.user.email", is(equalTo("test@mail.com"))))
					.andExpect(jsonPath("$.skills[?(@.id=='123')].name", hasItem("Spring Boot")))
					.andExpect(jsonPath("$.skills[?(@.id=='456')].name", hasItem("Java")))
					.andExpect(jsonPath("$.project.id", is(equalTo("777"))))
					.andExpect(jsonPath("$.project.name", is(equalTo("Project"))))
					.andExpect(jsonPath("$.project.industrySector", is(equalTo("Information Technology"))))
					.andExpect(jsonPath("$.project.customer", is(equalTo("Customer"))))
					.andExpect(jsonPath("$.project.description", is(equalTo("Description"))))
					.andExpect(jsonPath("$.project.creationDate", is(equalTo("2019-01-10T10:00:00"))))
					.andExpect(jsonPath("$.project.lastModifiedDate", is(equalTo("2019-01-10T11:00:00"))));
		}
	}

	@DisplayName("User project can be approved by user's manager.")
	@Test
	void userProjectCanBeApprovedByManager() throws Exception {
		final ClassPathResource body = new ClassPathResource("user-project/approve-user-project.json");
		final User manager = User.builder()
				.id("9cc7fab3-49b8-4d40-bf8a-ea5cad71c5f3")
				.userName("manager")
				.build();
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.firstName("test")
				.lastName("testing")
				.email("test@mail.com")
				.userName("tester")
				.manager(manager)
				.build();
		given(skillQueryService.convertSkillNamesToSkillsSet(new HashSet<>(Arrays.asList("Spring Boot", "Java")))).willReturn(new HashSet<>(
				Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.id("456")
								.name("Java")
								.build()
				)
		));
		given(userProjectCommandService.updateUserProject("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123", UpdateUserProjectCommand.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("456")
										.name("Java")
										.build()
						)
				))
				.approved(true)
				.build()
		)).willReturn(UserProject.builder()
				.id("bbb")
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.creationDate(LocalDateTime.of(2019, 1, 20, 9, 30))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 11, 0))
				.user(owner)
				.project(Project.builder()
						.id("777")
						.name("Project")
						.customer("Customer")
						.industrySector("Information Technology")
						.creationDate(LocalDateTime.of(2019, 1, 10, 10, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 10, 11, 0))
						.description("Description")
						.build()
				)
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("456")
										.name("Java")
										.build()
						)
				))
				.build()
		);
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(owner));
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123").contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(manager)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id", is(equalTo("bbb"))))
					.andExpect(jsonPath("$.role", is(equalTo("developer"))))
					.andExpect(jsonPath("$.tasks", is(equalTo("development"))))
					.andExpect(jsonPath("$.startDate", is(equalTo("2019-01-09"))))
					.andExpect(jsonPath("$.endDate", is(equalTo("2019-05-01"))))
					.andExpect(jsonPath("$.creationDate", is(equalTo("2019-01-20T09:30:00"))))
					.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-01-20T11:00:00"))))
					.andExpect(jsonPath("$.user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.user.firstName", is(equalTo("test"))))
					.andExpect(jsonPath("$.user.lastName", is(equalTo("testing"))))
					.andExpect(jsonPath("$.user.email", is(equalTo("test@mail.com"))))
					.andExpect(jsonPath("$.skills[?(@.id=='123')].name", hasItem("Spring Boot")))
					.andExpect(jsonPath("$.skills[?(@.id=='456')].name", hasItem("Java")))
					.andExpect(jsonPath("$.project.id", is(equalTo("777"))))
					.andExpect(jsonPath("$.project.name", is(equalTo("Project"))))
					.andExpect(jsonPath("$.project.industrySector", is(equalTo("Information Technology"))))
					.andExpect(jsonPath("$.project.customer", is(equalTo("Customer"))))
					.andExpect(jsonPath("$.project.description", is(equalTo("Description"))))
					.andExpect(jsonPath("$.project.creationDate", is(equalTo("2019-01-10T10:00:00"))))
					.andExpect(jsonPath("$.project.lastModifiedDate", is(equalTo("2019-01-10T11:00:00"))));
		}
	}

	@DisplayName("The user cannot approve her / his project membership on her / his own.")
	@Test
	void userCannotApproveHerProjectMembershipOnHerOwn() throws Exception {
		final ClassPathResource body = new ClassPathResource("user-project/approve-user-project.json");
		given(skillQueryService.convertSkillNamesToSkillsSet(new HashSet<>(Arrays.asList("Spring Boot", "Java")))).willReturn(new HashSet<>(
				Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.name("Java")
								.build()
				)
		));
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(owner));
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123").contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

}
