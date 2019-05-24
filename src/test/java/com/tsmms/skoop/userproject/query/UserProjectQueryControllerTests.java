package com.tsmms.skoop.userproject.query;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.userproject.UserProject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProjectQueryController.class)
class UserProjectQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserProjectQueryService userProjectQueryService;

	@MockBean
	private UserQueryService userQueryService;

	@Test
	@DisplayName("Tests if user projects can be retrieved.")
	void testIfUserProjectsCanBeRetrieved() throws Exception {
		given(userProjectQueryService.getUserProjects("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Stream.of(
						UserProject.builder()
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
								.build(),
						UserProject.builder()
								.role("designer")
								.tasks("design")
								.startDate(LocalDate.of(2018, 1, 9))
								.endDate(LocalDate.of(2018, 5, 1))
								.id("bbb")
								.creationDate(LocalDateTime.of(2018, 11, 3, 13, 30))
								.lastModifiedDate(LocalDateTime.of(2018, 11, 3, 13, 31))
								.user(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.firstName("test")
										.lastName("testing")
										.email("test@mail.com")
										.build()
								)
								.project(Project.builder()
										.id("456")
										.name("Another project")
										.industrySector("Software development")
										.customer("Another company")
										.creationDate(LocalDateTime.of(2018, 1, 11, 11, 30))
										.lastModifiedDate(LocalDateTime.of(2018, 1, 12, 10, 10))
										.description("Some project description")
										.build()
								)
								.build()
				));
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Optional.of(owner)
		);

		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("aaa"))))
				.andExpect(jsonPath("$[0].role", is(equalTo("developer"))))
				.andExpect(jsonPath("$[0].tasks", is(equalTo("development"))))
				.andExpect(jsonPath("$[0].startDate", is(equalTo("2019-01-09"))))
				.andExpect(jsonPath("$[0].endDate", is(equalTo("2019-05-01"))))
				.andExpect(jsonPath("$[0].creationDate", is(equalTo("2019-11-03T13:30:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDate", is(equalTo("2019-11-03T13:31:00"))))
				.andExpect(jsonPath("$[0].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[0].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[0].project.id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].project.name", is(equalTo("Simple project"))))
				.andExpect(jsonPath("$[0].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[0].project.customer", is(equalTo("Company"))))
				.andExpect(jsonPath("$[0].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[0].project.creationDate", is(equalTo("2019-01-11T11:30:00"))))
				.andExpect(jsonPath("$[0].project.lastModifiedDate", is(equalTo("2019-01-12T10:10:00"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("bbb"))))
				.andExpect(jsonPath("$[1].role", is(equalTo("designer"))))
				.andExpect(jsonPath("$[1].tasks", is(equalTo("design"))))
				.andExpect(jsonPath("$[1].startDate", is(equalTo("2018-01-09"))))
				.andExpect(jsonPath("$[1].endDate", is(equalTo("2018-05-01"))))
				.andExpect(jsonPath("$[1].creationDate", is(equalTo("2018-11-03T13:30:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDate", is(equalTo("2018-11-03T13:31:00"))))
				.andExpect(jsonPath("$[1].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[1].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[1].project.id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].project.name", is(equalTo("Another project"))))
				.andExpect(jsonPath("$[1].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[1].project.customer", is(equalTo("Another company"))))
				.andExpect(jsonPath("$[1].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[1].project.creationDate", is(equalTo("2018-01-11T11:30:00"))))
				.andExpect(jsonPath("$[1].project.lastModifiedDate", is(equalTo("2018-01-12T10:10:00"))));
	}

	@Test
	@DisplayName("Tests if user projects cannot be fetched by unauthorized user.")
	void testIfUserProjectsCannotBeFetchedByUnauthorizedUser() throws Exception {
		final User owner = User.builder()
				.id("2a29ca1o-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Optional.of(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.firstName("test")
						.lastName("testing")
						.email("test@mail.com")
						.build()
				)
		);
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if user projects cannot be fetched by not authenticated user.")
	void testIfUserProjectsCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Tests if user projects can be retrieved by different but authorized user.")
	void testIfUserProjectsCanBeRetrievedByDifferentButAuthorizedUser() throws Exception {
		given(userProjectQueryService.getUserProjects("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Stream.of(
						UserProject.builder()
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
								.build(),
						UserProject.builder()
								.role("designer")
								.tasks("design")
								.startDate(LocalDate.of(2018, 1, 9))
								.endDate(LocalDate.of(2018, 5, 1))
								.id("bbb")
								.creationDate(LocalDateTime.of(2018, 11, 3, 13, 30))
								.lastModifiedDate(LocalDateTime.of(2018, 11, 3, 13, 31))
								.user(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.firstName("test")
										.lastName("testing")
										.email("test@mail.com")
										.build()
								)
								.project(Project.builder()
										.id("456")
										.name("Another project")
										.industrySector("Software development")
										.customer("Another company")
										.creationDate(LocalDateTime.of(2018, 1, 11, 11, 30))
										.lastModifiedDate(LocalDateTime.of(2018, 1, 12, 10, 10))
										.description("Some project description")
										.build()
								)
								.build()
				));
		final User owner = User.builder()
				.id("2a29ca1o-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		givenUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.hasAuthorizedUsers("2a29ca1o-b4d0-4119-9113-4677beb20ae2")
				.forScopes(UserPermissionScope.READ_USER_SKILLS);

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Optional.of(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.firstName("test")
						.lastName("testing")
						.email("test@mail.com")
						.build()
				)
		);

		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("aaa"))))
				.andExpect(jsonPath("$[0].role", is(equalTo("developer"))))
				.andExpect(jsonPath("$[0].tasks", is(equalTo("development"))))
				.andExpect(jsonPath("$[0].startDate", is(equalTo("2019-01-09"))))
				.andExpect(jsonPath("$[0].endDate", is(equalTo("2019-05-01"))))
				.andExpect(jsonPath("$[0].creationDate", is(equalTo("2019-11-03T13:30:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDate", is(equalTo("2019-11-03T13:31:00"))))
				.andExpect(jsonPath("$[0].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[0].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[0].project.id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].project.name", is(equalTo("Simple project"))))
				.andExpect(jsonPath("$[0].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[0].project.customer", is(equalTo("Company"))))
				.andExpect(jsonPath("$[0].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[0].project.creationDate", is(equalTo("2019-01-11T11:30:00"))))
				.andExpect(jsonPath("$[0].project.lastModifiedDate", is(equalTo("2019-01-12T10:10:00"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("bbb"))))
				.andExpect(jsonPath("$[1].role", is(equalTo("designer"))))
				.andExpect(jsonPath("$[1].tasks", is(equalTo("design"))))
				.andExpect(jsonPath("$[1].startDate", is(equalTo("2018-01-09"))))
				.andExpect(jsonPath("$[1].endDate", is(equalTo("2018-05-01"))))
				.andExpect(jsonPath("$[1].creationDate", is(equalTo("2018-11-03T13:30:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDate", is(equalTo("2018-11-03T13:31:00"))))
				.andExpect(jsonPath("$[1].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[1].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[1].project.id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].project.name", is(equalTo("Another project"))))
				.andExpect(jsonPath("$[1].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[1].project.customer", is(equalTo("Another company"))))
				.andExpect(jsonPath("$[1].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[1].project.creationDate", is(equalTo("2018-01-11T11:30:00"))))
				.andExpect(jsonPath("$[1].project.lastModifiedDate", is(equalTo("2018-01-12T10:10:00"))));
	}

	@Test
	@DisplayName("Tests if specific user project can be retrieved.")
	void testIfSpecificUserProjectCanBeRetrieved() throws Exception {
		given(userProjectQueryService.getUserProjectByUserIdAndProjectId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123"))
				.willReturn(Optional.of(
						UserProject.builder()
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
				));
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
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

	@Test
	@DisplayName("Tests if specific user project cannot be fetched by unauthorized user.")
	void testIfSpecificUserProjectCannotBeFetchedByUnauthorizedUser() throws Exception {
		final User owner = User.builder()
				.id("2a29ca1o-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if specific user project cannot be fetched by not authenticated user.")
	void testIfSpecificUserProjectCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Not found status code is returned when there is no user-project relationship.")
	void notFoundStatusCodeIsReturnedWhenThereIsNoUserProjectRelationship() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(userProjectQueryService.getUserProjectByUserIdAndProjectId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123"))
				.willReturn(Optional.empty());
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Tests if user projects can be retrieved by user manager.")
	void testIfUserProjectsCanBeRetrievedByUserManager() throws Exception {
		given(userProjectQueryService.getUserProjects("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Stream.of(
						UserProject.builder()
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
								.build(),
						UserProject.builder()
								.role("designer")
								.tasks("design")
								.startDate(LocalDate.of(2018, 1, 9))
								.endDate(LocalDate.of(2018, 5, 1))
								.id("bbb")
								.creationDate(LocalDateTime.of(2018, 11, 3, 13, 30))
								.lastModifiedDate(LocalDateTime.of(2018, 11, 3, 13, 31))
								.user(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.firstName("test")
										.lastName("testing")
										.email("test@mail.com")
										.build()
								)
								.project(Project.builder()
										.id("456")
										.name("Another project")
										.industrySector("Software development")
										.customer("Another company")
										.creationDate(LocalDateTime.of(2018, 1, 11, 11, 30))
										.lastModifiedDate(LocalDateTime.of(2018, 1, 12, 10, 10))
										.description("Some project description")
										.build()
								)
								.build()
				));
		final User manager = User.builder()
				.id("2a29ca1o-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Optional.of(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.firstName("test")
						.lastName("testing")
						.email("test@mail.com")
						.manager(manager)
						.build()
				)
		);

		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/projects")
				.with(authentication(withUser(manager))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("aaa"))))
				.andExpect(jsonPath("$[0].role", is(equalTo("developer"))))
				.andExpect(jsonPath("$[0].tasks", is(equalTo("development"))))
				.andExpect(jsonPath("$[0].startDate", is(equalTo("2019-01-09"))))
				.andExpect(jsonPath("$[0].endDate", is(equalTo("2019-05-01"))))
				.andExpect(jsonPath("$[0].creationDate", is(equalTo("2019-11-03T13:30:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDate", is(equalTo("2019-11-03T13:31:00"))))
				.andExpect(jsonPath("$[0].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[0].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[0].project.id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].project.name", is(equalTo("Simple project"))))
				.andExpect(jsonPath("$[0].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[0].project.customer", is(equalTo("Company"))))
				.andExpect(jsonPath("$[0].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[0].project.creationDate", is(equalTo("2019-01-11T11:30:00"))))
				.andExpect(jsonPath("$[0].project.lastModifiedDate", is(equalTo("2019-01-12T10:10:00"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("bbb"))))
				.andExpect(jsonPath("$[1].role", is(equalTo("designer"))))
				.andExpect(jsonPath("$[1].tasks", is(equalTo("design"))))
				.andExpect(jsonPath("$[1].startDate", is(equalTo("2018-01-09"))))
				.andExpect(jsonPath("$[1].endDate", is(equalTo("2018-05-01"))))
				.andExpect(jsonPath("$[1].creationDate", is(equalTo("2018-11-03T13:30:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDate", is(equalTo("2018-11-03T13:31:00"))))
				.andExpect(jsonPath("$[1].user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].user.firstName", is(equalTo("test"))))
				.andExpect(jsonPath("$[1].user.lastName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].user.email", is(equalTo("test@mail.com"))))
				.andExpect(jsonPath("$[1].project.id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].project.name", is(equalTo("Another project"))))
				.andExpect(jsonPath("$[1].project.industrySector", is(equalTo("Software development"))))
				.andExpect(jsonPath("$[1].project.customer", is(equalTo("Another company"))))
				.andExpect(jsonPath("$[1].project.description", is(equalTo("Some project description"))))
				.andExpect(jsonPath("$[1].project.creationDate", is(equalTo("2018-01-11T11:30:00"))))
				.andExpect(jsonPath("$[1].project.lastModifiedDate", is(equalTo("2018-01-12T10:10:00"))));
	}

}
