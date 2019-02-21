package io.knowledgeassets.myskills.server.userproject.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userproject.UserProject;
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

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
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

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserProjectCommandService userProjectCommandService;

	@Test
	@DisplayName("Tests if a project can be assigned to a user")
	void testIfProjectCanBeAssignedToUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("assign-project-to-user.json");
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(userProjectCommandService.assignProjectToUser("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2", UserProject.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.build()
		)).willReturn(UserProject.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.id(732L)
				.creationDate(LocalDateTime.of(2019, 11, 3, 13, 30))
				.lastModifiedDate(LocalDateTime.of(2019, 11, 3, 13, 31))
				.user(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.firstName("test")
						.lastName("testing")
						.coach(false)
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
					.andExpect(jsonPath("$.id", is(equalTo(732))))
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
					.andExpect(jsonPath("$.user.coach", is(equalTo(false))))
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
	@DisplayName("Tests if user project can be updated.")
	void testIfUserProjectCanBeUpdated() throws Exception {
		final ClassPathResource body = new ClassPathResource("update-user-project.json");
		given(userProjectCommandService.updateUserProject("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123", UpdateUserProjectCommand.builder()
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.build()
		)).willReturn(UserProject.builder()
				.id(123L)
				.role("developer")
				.tasks("development")
				.startDate(LocalDate.of(2019, 1, 9))
				.endDate(LocalDate.of(2019, 5, 1))
				.creationDate(LocalDateTime.of(2019, 1, 20, 9, 30))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 11, 0))
				.user(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.firstName("test")
						.lastName("testing")
						.email("test@mail.com")
						.userName("tester")
						.coach(false)
						.build()
				)
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
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123").contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id", is(equalTo(123))))
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
					.andExpect(jsonPath("$.user.coach", is(equalTo(false))))
					.andExpect(jsonPath("$.user.email", is(equalTo("test@mail.com"))))
					.andExpect(jsonPath("$.project.id", is(equalTo("777"))))
					.andExpect(jsonPath("$.project.name", is(equalTo("Project"))))
					.andExpect(jsonPath("$.project.industrySector", is(equalTo("Information Technology"))))
					.andExpect(jsonPath("$.project.customer", is(equalTo("Customer"))))
					.andExpect(jsonPath("$.project.description", is(equalTo("Description"))))
					.andExpect(jsonPath("$.project.creationDate", is(equalTo("2019-01-10T10:00:00"))))
					.andExpect(jsonPath("$.project.lastModifiedDate", is(equalTo("2019-01-10T11:00:00"))));
		}
	}

}
