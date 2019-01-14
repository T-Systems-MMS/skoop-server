package io.knowledgeassets.myskills.server.project.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectQueryController.class)
class ProjectQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProjectQueryService projectQueryService;

	@Test
	@DisplayName("Tests if all projects can be fetched.")
	void testIfAllProjectsAreFetched() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(projectQueryService.getProjects()).willReturn(
				Stream.of(
						Project.builder()
								.id("123")
								.name("First development Project")
								.customer("Some first customer")
								.industrySector("Information Technology")
								.description("Description of the first project")
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 12, 30))
								.build(),
						Project.builder()
								.id("456")
								.name("Second development Project")
								.customer("Some second customer")
								.industrySector("Telecommunications")
								.description("Description of the second project")
								.creationDate(LocalDateTime.of(2019, 1, 10, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 10, 11, 30))
								.build()
				)
		);
		mockMvc.perform(get("/projects")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].name", is(equalTo("First development Project"))))
				.andExpect(jsonPath("$[0].customer", is(equalTo("Some first customer"))))
				.andExpect(jsonPath("$[0].industrySector", is(equalTo("Information Technology"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("Description of the first project"))))
				.andExpect(jsonPath("$[0].creationDate", is(equalTo("2019-01-09T10:30:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDate", is(equalTo("2019-01-09T12:30:00"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].name", is(equalTo("Second development Project"))))
				.andExpect(jsonPath("$[1].customer", is(equalTo("Some second customer"))))
				.andExpect(jsonPath("$[1].industrySector", is(equalTo("Telecommunications"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Description of the second project"))))
				.andExpect(jsonPath("$[1].creationDate", is(equalTo("2019-01-10T10:30:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDate", is(equalTo("2019-01-10T11:30:00"))));
	}

	@Test
	@DisplayName("Tests if a project can be fetched by identifier.")
	void testIfProjectIsFetchedByIdentifier() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(projectQueryService.getProjectById("456")).willReturn(
				Optional.of(
						Project.builder()
								.id("456")
								.name("Second development Project")
								.customer("Some second customer")
								.industrySector("Telecommunications")
								.description("Description of the second project")
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.build()
				)
		);
		mockMvc.perform(get("/projects/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("456"))))
				.andExpect(jsonPath("$.name", is(equalTo("Second development Project"))))
				.andExpect(jsonPath("$.customer", is(equalTo("Some second customer"))))
				.andExpect(jsonPath("$.industrySector", is(equalTo("Telecommunications"))))
				.andExpect(jsonPath("$.description", is(equalTo("Description of the second project"))))
				.andExpect(jsonPath("$.creationDate", is(equalTo("2019-01-09T10:30:00"))))
				.andExpect(jsonPath("$.lastModifiedDate", is(equalTo("2019-01-09T11:30:00"))));
	}

	@Test
	@DisplayName("Tests if not found status code is returned in case project was not found by identifier.")
	void testIfNotFoundStatusCodeIsReturnedInCaseProjectWasNotFoundByIdentifier() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(projectQueryService.getProjectById("456")).willReturn(Optional.empty());
		mockMvc.perform(get("/projects/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned in case project cannot be fetched by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedInCaseProjectCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/projects/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned in case all projects cannot be fetched by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedInCaseAllProjectsCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/projects")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

}
