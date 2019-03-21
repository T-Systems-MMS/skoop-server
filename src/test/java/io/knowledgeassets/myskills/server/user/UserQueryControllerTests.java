package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import io.knowledgeassets.myskills.server.user.query.UserQueryController;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static io.knowledgeassets.myskills.server.user.UserPermissionScope.READ_USER_SKILLS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserQueryController.class)
class UserQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private UserPermissionQueryService userPermissionQueryService;

	private User owner;

	@BeforeEach
	void setup() {
		this.owner = User.builder()
			.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
			.userName("tester")
			.build();
	}

	@Test
	@DisplayName("Responds with the list of users with additional fields present for requesting user")
	void respondsWithListOfUsers() throws Exception {
		given(userQueryService.getUsers()).willReturn(Stream.of(
				User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Architect")
						.summary("Toni's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("Management", "Software Integration"))
						.certificates(Collections.singletonList("Java Certified Programmer"))
						.languages(Collections.singletonList("Deutsch"))
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("testing")
						.firstName("Tina")
						.lastName("Testing")
						.email("tina.testing@myskills.io")
						.coach(false)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Engineer")
						.summary("Tina's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("IT Consulting", "Software Integration"))
						.certificates(Collections.singletonList("Kotlin Certified Programmer"))
						.languages(Collections.singletonList("English"))
						.build()
		));

		mockMvc.perform(get("/users")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$[0].coach", is(equalTo(true))))
				.andExpect(jsonPath("$[0].academicDegree", is(equalTo("Diplom-Wirtschaftsinformatiker"))))
				.andExpect(jsonPath("$[0].positionProfile", is(equalTo("Software Architect"))))
				.andExpect(jsonPath("$[0].summary", is(equalTo("Toni's summary"))))
				.andExpect(jsonPath("$[0].industrySectors", is(equalTo(Arrays.asList("Automotive", "Telecommunication")))))
				.andExpect(jsonPath("$[0].specializations", is(equalTo(Arrays.asList("Management", "Software Integration")))))
				.andExpect(jsonPath("$[0].certificates", is(equalTo(Collections.singletonList("Java Certified Programmer")))))
				.andExpect(jsonPath("$[0].languages", is(equalTo(Collections.singletonList("Deutsch")))))
				.andExpect(jsonPath("$[1].id", is(equalTo("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tina.testing@myskills.io"))))
				.andExpect(jsonPath("$[1].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[1].academicDegree", nullValue()))
				.andExpect(jsonPath("$[1].positionProfile", nullValue()))
				.andExpect(jsonPath("$[1].summary", nullValue()))
				.andExpect(jsonPath("$[1].industrySectors", nullValue()))
				.andExpect(jsonPath("$[1].specializations", nullValue()))
				.andExpect(jsonPath("$[1].certificates", nullValue()))
				.andExpect(jsonPath("$[1].languages", nullValue()));
	}

	@Test
	@DisplayName("Responds with the requested user")
	void respondsWithRequestedUser() throws Exception {
		given(userQueryService.getUserById("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Optional.of(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.build())
				);

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$.firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$.lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$.email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$.coach", is(equalTo(true))))
				.andExpect(jsonPath("$.academicDegree", nullValue()))
				.andExpect(jsonPath("$.positionProfile", nullValue()))
				.andExpect(jsonPath("$.summary", nullValue()))
				.andExpect(jsonPath("$.industrySectors", nullValue()))
				.andExpect(jsonPath("$.specializations", nullValue()))
				.andExpect(jsonPath("$.certificates", nullValue()))
				.andExpect(jsonPath("$.languages", nullValue()));
	}

	@Test
	@DisplayName("Responds with the requested user with additional fields in case requesting user is the same.")
	void respondsWithRequestedUserWithAdditionalFieldsInCaseRequestingUserIsTheSame() throws Exception {
		given(userQueryService.getUserById("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Optional.of(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Architect")
						.summary("Toni's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("Management", "Software Integration"))
						.certificates(Collections.singletonList("Java Certified Programmer"))
						.languages(Collections.singletonList("Deutsch"))
						.build())
				);

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$.firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$.lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$.email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$.coach", is(equalTo(true))))
				.andExpect(jsonPath("$.academicDegree", is(equalTo("Diplom-Wirtschaftsinformatiker"))))
				.andExpect(jsonPath("$.positionProfile", is(equalTo("Software Architect"))))
				.andExpect(jsonPath("$.summary", is(equalTo("Toni's summary"))))
				.andExpect(jsonPath("$.industrySectors", is(equalTo(Arrays.asList("Automotive", "Telecommunication")))))
				.andExpect(jsonPath("$.specializations", is(equalTo(Arrays.asList("Management", "Software Integration")))))
				.andExpect(jsonPath("$.certificates", is(equalTo(Collections.singletonList("Java Certified Programmer")))))
				.andExpect(jsonPath("$.languages", is(equalTo(Collections.singletonList("Deutsch")))));
	}

	@Test
	@DisplayName("Responds with the list of users with additional fields present both for requesting user and for user allowed to see her skills")
	void respondsWithListOfUsersWithAdditionalFieldsPresentBothForRequestingUserAndForUserAllowedToSeeHerSkills() throws Exception {
		given(userPermissionQueryService.getUsersWhoGrantedPermission("56ef4778-a084-4509-9a3e-80b7895cf7b0", READ_USER_SKILLS))
				.willReturn(Stream.of(
					User.builder()
							.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.build()
				));

		given(userQueryService.getUsers()).willReturn(Stream.of(
				User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Architect")
						.summary("Toni's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("Management", "Software Integration"))
						.certificates(Collections.singletonList("Java Certified Programmer"))
						.languages(Collections.singletonList("Deutsch"))
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("testing")
						.firstName("Tina")
						.lastName("Testing")
						.email("tina.testing@myskills.io")
						.coach(false)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Engineer")
						.summary("Tina's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("IT Consulting", "Software Integration"))
						.certificates(Collections.singletonList("Kotlin Certified Programmer"))
						.languages(Collections.singletonList("English"))
						.build()
		));

		mockMvc.perform(get("/users")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$[0].coach", is(equalTo(true))))
				.andExpect(jsonPath("$[0].academicDegree", is(equalTo("Diplom-Wirtschaftsinformatiker"))))
				.andExpect(jsonPath("$[0].positionProfile", is(equalTo("Software Architect"))))
				.andExpect(jsonPath("$[0].summary", is(equalTo("Toni's summary"))))
				.andExpect(jsonPath("$[0].industrySectors", is(equalTo(Arrays.asList("Automotive", "Telecommunication")))))
				.andExpect(jsonPath("$[0].specializations", is(equalTo(Arrays.asList("Management", "Software Integration")))))
				.andExpect(jsonPath("$[0].certificates", is(equalTo(Collections.singletonList("Java Certified Programmer")))))
				.andExpect(jsonPath("$[0].languages", is(equalTo(Collections.singletonList("Deutsch")))))
				.andExpect(jsonPath("$[1].id", is(equalTo("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tina.testing@myskills.io"))))
				.andExpect(jsonPath("$[1].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[1].academicDegree", is(equalTo("Diplom-Wirtschaftsinformatiker"))))
				.andExpect(jsonPath("$[1].positionProfile", is(equalTo("Software Engineer"))))
				.andExpect(jsonPath("$[1].summary", is(equalTo("Tina's summary"))))
				.andExpect(jsonPath("$[1].industrySectors", is(equalTo(Arrays.asList("Automotive", "Telecommunication")))))
				.andExpect(jsonPath("$[1].specializations", is(equalTo(Arrays.asList("IT Consulting", "Software Integration")))))
				.andExpect(jsonPath("$[1].certificates", is(equalTo(Collections.singletonList("Kotlin Certified Programmer")))))
				.andExpect(jsonPath("$[1].languages", is(equalTo(Collections.singletonList("English")))));
	}

}
