package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserPermission;
import io.knowledgeassets.myskills.server.user.UserPermissionScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserPermissionQueryController.class)
class UserPermissionQueryControllerTests extends AbstractControllerTests {

	@MockBean
	private UserPermissionQueryService userPermissionQueryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Tests getting all outbound user permissions granted to other users.")
	void testGettingAllOutboundUserPermissionsGrantedToOtherUsers() throws Exception {
		final User owner = User.builder()
				.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		final User tester = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("marydoe")
				.firstName("Mary")
				.lastName("Doe")
				.email("mary.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Architect")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Scala Certified Programmer"))
				.languages(Collections.singletonList("English"))
				.build();

		given(userPermissionQueryService.getOutboundUserPermissionsByOwnerId("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))
		.willReturn(Stream.of(
				UserPermission.builder()
					.owner(owner)
					.scope(UserPermissionScope.READ_USER_SKILLS)
					.authorizedUsers(Collections.singletonList(tester))
					.id("4af0d371-364e-46c7-881b-e96c6385103e")
				.build()
		));
		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/outbound-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].owner.id", is(equalTo("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].owner.userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$[0].owner.firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$[0].owner.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].owner.email", is(equalTo("john.doe@mail.com"))))
				.andExpect(jsonPath("$[0].owner.coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].owner.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].owner.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].owner.summary").doesNotExist())
				.andExpect(jsonPath("$[0].owner.industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].owner.specializations").doesNotExist())
				.andExpect(jsonPath("$[0].owner.certificates").doesNotExist())
				.andExpect(jsonPath("$[0].owner.languages").doesNotExist())
				.andExpect(jsonPath("$[0].scope", is(equalTo("READ_USER_SKILLS"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].id", is(equalTo("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].userName", is(equalTo("marydoe"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].firstName", is(equalTo("Mary"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].email", is(equalTo("mary.doe@mail.com"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].summary").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].specializations").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].certificates").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].languages").doesNotExist());
	}

	@Test
	@DisplayName("Tests getting all inbound user permissions granted by other users.")
	void testGettingAllInboundUserPermissionsGrantedByOtherUsers() throws Exception {
		final User owner = User.builder()
				.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		final User tester = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("marydoe")
				.firstName("Mary")
				.lastName("Doe")
				.email("mary.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Architect")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Scala Certified Programmer"))
				.languages(Collections.singletonList("English"))
				.build();

		given(userPermissionQueryService.getInboundUserPermissionsByAuthorizedUserId("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))
				.willReturn(Stream.of(
						UserPermission.builder()
								.owner(tester)
								.scope(UserPermissionScope.READ_USER_SKILLS)
								.authorizedUsers(Collections.singletonList(owner))
								.id("4af0d371-364e-46c7-881b-e96c6385103e")
								.build()
				));
		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].owner.id", is(equalTo("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].owner.userName", is(equalTo("marydoe"))))
				.andExpect(jsonPath("$[0].owner.firstName", is(equalTo("Mary"))))
				.andExpect(jsonPath("$[0].owner.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].owner.email", is(equalTo("mary.doe@mail.com"))))
				.andExpect(jsonPath("$[0].owner.coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].owner.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].owner.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].owner.summary").doesNotExist())
				.andExpect(jsonPath("$[0].owner.industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].owner.specializations").doesNotExist())
				.andExpect(jsonPath("$[0].owner.certificates").doesNotExist())
				.andExpect(jsonPath("$[0].owner.languages").doesNotExist())
				.andExpect(jsonPath("$[0].scope", is(equalTo("READ_USER_SKILLS"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].id", is(equalTo("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].email", is(equalTo("john.doe@mail.com"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].summary").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].specializations").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].certificates").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].languages").doesNotExist());
	}

	@Test
	@DisplayName("Tests if forbidden (403) status code is returned when a user requests inbound permissions of another user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenUserRequestsInboundPermissionsOfAnotherUser() throws Exception {
		final User owner = User.builder()
				.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if unauthorized (401) status code is returned when an anonymous user requests inbound permissions of a user.")
	void testIfUnauthorizedStatusCodeIsReturnedWhenAnonymousUserRequestsInboundPermissionsOfUser() throws Exception {
		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-permissions")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

}
