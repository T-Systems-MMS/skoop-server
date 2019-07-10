package com.tsmms.skoop.project.command;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.common.AbstractControllerTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.time.LocalDateTime;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectCommandController.class)
class ProjectCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProjectCommandService projectCommandService;

	@Test
	@DisplayName("Project can be created.")
	void projectIsCreated() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("create-project.json");
		final Project project = Project.builder()
				.name("Development Project")
				.customer("Some Customer")
				.industrySector("Information Technology")
				.description("Description of the project")
				.build();
		given(projectCommandService.create(project)).willReturn(
				Project.builder()
						.id("123")
						.name(project.getName())
						.customer(project.getCustomer())
						.industrySector(project.getIndustrySector())
						.description(project.getDescription())
						.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
						.build()
		);
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/projects")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.name", is(equalTo("Development Project"))))
					.andExpect(jsonPath("$.customer", is(equalTo("Some Customer"))))
					.andExpect(jsonPath("$.industrySector", is(equalTo("Information Technology"))))
					.andExpect(jsonPath("$.description", is(equalTo("Description of the project"))))
					.andExpect(jsonPath("$.creationDate", is(equalTo("2019-01-09T10:30:00"))))
					.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-01-09T11:30:00"))));
		}
	}

	@Test
	@DisplayName("Project cannot be created when CSRF token is not present.")
	void projectCannotBeCreatedWhenCsrfTokenIsNotPresent() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("create-project.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/projects")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner))))
					.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("Project cannot be created when an invalid CSRF token is used.")
	void projectCannotBeCreatedWhenInvalidCsrfTokenIsUsed() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("create-project.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/projects")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf().useInvalidToken()))
					.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when project is created by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenProjectIsCreatedByNotAuthenticatedUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("create-project.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/projects")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("Tests if bad request status code is returned in case project has an empty name when creating a project.")
	void testIfBadRequestStatusCodeIsReturnedInCaseProjectHasEmptyNameWhenCreatingProject() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("create-project-with-empty-name.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/projects")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if bad request status code is returned in case project has an empty name when updating a project.")
	void testIfBadRequestStatusCodeIsReturnedInCaseProjectHasEmptyNameWhenUpdatingProject() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("update-project-with-empty-name.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/projects/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if a project can be updated.")
	void testIfProjectIsUpdated() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("update-project.json");
		final UpdateProjectCommand updateProjectCommand = UpdateProjectCommand.builder()
				.id("123")
				.name("Development Project")
				.customer("Some Customer")
				.industrySector("Information Technology")
				.description("New description of the project")
				.build();
		given(projectCommandService.update(updateProjectCommand)).willReturn(
				Project.builder()
						.id("123")
						.name(updateProjectCommand.getName())
						.customer(updateProjectCommand.getCustomer())
						.industrySector(updateProjectCommand.getIndustrySector())
						.description(updateProjectCommand.getDescription())
						.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
						.build()
		);
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/projects/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.name", is(equalTo("Development Project"))))
					.andExpect(jsonPath("$.customer", is(equalTo("Some Customer"))))
					.andExpect(jsonPath("$.industrySector", is(equalTo("Information Technology"))))
					.andExpect(jsonPath("$.description", is(equalTo("New description of the project"))))
					.andExpect(jsonPath("$.creationDate", is(equalTo("2019-01-09T10:30:00"))))
					.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-01-09T11:30:00"))));
		}
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when project is updated by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenProjectIsUpdatedByNotAuthenticatedUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("update-project.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/projects/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("Tests if the project can be deleted.")
	void testIfProjectCanBeDeleted() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/projects/123")
				.with(authentication(withUser(owner, "ADMIN")))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Tests if the project cannot be deleted by unauthorized user.")
	void testIfProjectCannotBeDeletedByUnauthorizedUser() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/projects/123")
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when project is deleted by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenProjectIsDeletedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(delete("/projects/123")
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

}
