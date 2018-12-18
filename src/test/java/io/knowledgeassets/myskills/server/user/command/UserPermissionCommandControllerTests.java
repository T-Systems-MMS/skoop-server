package io.knowledgeassets.myskills.server.user.command;

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

import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPermissionCommandController.class)
class UserPermissionCommandControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserPermissionCommandService userPermissionCommandService;

	@Test
	@DisplayName("Stores and returns the given list of user permissions")
	void storesAndReturnsListOfUserPermissions() throws Exception {
		User owner = User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.firstName("Toni")
				.lastName("Tester")
				.email("toni.tester@myskills.io")
				.coach(true)
				.build();

		ReplaceUserPermissionListCommand expectedCommand = ReplaceUserPermissionListCommand.builder()
				.ownerId("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userPermissions(singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_SKILLS)
						.authorizedUserIds(asList(
								"6aa4e666-6f40-4443-bf79-806472725b28",
								"e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc"
						))
						.build()))
				.build();

		willThrow(new IllegalArgumentException("Unexpected data in command object!"))
				.given(userPermissionCommandService).replaceUserPermissions(any());
		willReturn(Stream.of(UserPermission.builder()
				.scope(UserPermissionScope.READ_USER_SKILLS)
				.owner(owner)
				.authorizedUsers(asList(
						User.builder()
								.id("6aa4e666-6f40-4443-bf79-806472725b28")
								.userName("testing")
								.firstName("Tina")
								.lastName("Testing")
								.email("tina.testing@myskills.io")
								.coach(false)
								.build(),
						User.builder()
								.id("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc")
								.userName("testbed")
								.firstName("Tabia")
								.lastName("Testbed")
								.email("tabia.testbed@myskills.io")
								.coach(true)
								.build()
				))
				.build()))
				.given(userPermissionCommandService).replaceUserPermissions(expectedCommand);

		String requestContent = "[{" +
				"\"scope\":\"READ_USER_SKILLS\"," +
				"\"authorizedUserIds\":[" +
				"\"6aa4e666-6f40-4443-bf79-806472725b28\"," +
				"\"e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc\"" +
				"]" +
				"}]";

		mockMvc.perform(put("/users/db87d46a-e4ca-451a-903b-e8533e0b924b/permissions")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].owner", is(notNullValue())))
				.andExpect(jsonPath("$[0].owner.id", is(equalTo("db87d46a-e4ca-451a-903b-e8533e0b924b"))))
				.andExpect(jsonPath("$[0].owner.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].owner.firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].owner.lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].owner.email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$[0].owner.coach", is(equalTo(true))))
				.andExpect(jsonPath("$[0].scope", is(equalTo("READ_USER_SKILLS"))))
				.andExpect(jsonPath("$[0].authorizedUsers.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].id", is(equalTo("6aa4e666-6f40-4443-bf79-806472725b28"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].email", is(equalTo("tina.testing@myskills.io"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].id", is(equalTo("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].userName", is(equalTo("testbed"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].firstName", is(equalTo("Tabia"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].lastName", is(equalTo("Testbed"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].email", is(equalTo("tabia.testbed@myskills.io"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].coach", is(equalTo(true))));
	}

	@Test
	@DisplayName("Responds with status 403 if foreign user attempts to set permissions")
	void yieldsForbiddenOnForeignUserAuthentication() throws Exception {
		User foreigner = User.builder()
				.id("6aa4e666-6f40-4443-bf79-806472725b28")
				.userName("testing")
				.build();

		String requestContent = "[{" +
				"\"scope\":\"READ_USER_SKILLS\"," +
				"\"authorizedUserIds\":[" +
				"\"e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc\"" +
				"]" +
				"}]";

		mockMvc.perform(put("/users/db87d46a-e4ca-451a-903b-e8533e0b924b/permissions")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(foreigner)))
				.with(csrf()))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}
