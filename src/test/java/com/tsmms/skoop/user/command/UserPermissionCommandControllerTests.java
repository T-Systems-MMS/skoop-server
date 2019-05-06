package com.tsmms.skoop.user.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.common.JwtAuthenticationFactory;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
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
				.email("toni.tester@skoop.io")
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Architect")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
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
				.given(userPermissionCommandService).replaceOutboundUserPermissions(any());
		BDDMockito.willReturn(Stream.of(UserPermission.builder()
				.scope(UserPermissionScope.READ_USER_SKILLS)
				.owner(owner)
				.authorizedUsers(asList(
						User.builder()
								.id("6aa4e666-6f40-4443-bf79-806472725b28")
								.userName("testing")
								.firstName("Tina")
								.lastName("Testing")
								.email("tina.testing@skoop.io")
								.academicDegree("Diplom-Wirtschaftsinformatiker")
								.positionProfile("Software Developer")
								.summary("Developer")
								.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
								.specializations(Arrays.asList("IT Consulting", "Software Integration"))
								.certificates(Collections.singletonList("Scala Certified Programmer"))
								.languages(Collections.singletonList("English"))
								.build(),
						User.builder()
								.id("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc")
								.userName("testbed")
								.firstName("Tabia")
								.lastName("Testbed")
								.email("tabia.testbed@skoop.io")
								.academicDegree("Diplom-Wirtschaftsinformatiker")
								.positionProfile("Software Designer")
								.summary("Designer")
								.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
								.specializations(Arrays.asList("IT Consulting", "Software Integration"))
								.certificates(Collections.singletonList("Kotlin Certified Programmer"))
								.languages(Collections.singletonList("Scottish"))
								.build()
				))
				.build()))
				.given(userPermissionCommandService).replaceOutboundUserPermissions(expectedCommand);

		String requestContent = "[{" +
				"\"scope\":\"READ_USER_SKILLS\"," +
				"\"authorizedUserIds\":[" +
				"\"6aa4e666-6f40-4443-bf79-806472725b28\"," +
				"\"e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc\"" +
				"]" +
				"}]";

		mockMvc.perform(put("/users/db87d46a-e4ca-451a-903b-e8533e0b924b/outbound-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(JwtAuthenticationFactory.withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].owner", is(notNullValue())))
				.andExpect(jsonPath("$[0].owner.id", is(equalTo("db87d46a-e4ca-451a-903b-e8533e0b924b"))))
				.andExpect(jsonPath("$[0].owner.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].owner.firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].owner.lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].owner.email", is(equalTo("toni.tester@skoop.io"))))
				.andExpect(jsonPath("$[0].owner.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].owner.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].owner.summary").doesNotExist())
				.andExpect(jsonPath("$[0].owner.industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].owner.specializations").doesNotExist())
				.andExpect(jsonPath("$[0].owner.certificates").doesNotExist())
				.andExpect(jsonPath("$[0].owner.languages").doesNotExist())
				.andExpect(jsonPath("$[0].scope", is(equalTo("READ_USER_SKILLS"))))
				.andExpect(jsonPath("$[0].authorizedUsers.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].id", is(equalTo("6aa4e666-6f40-4443-bf79-806472725b28"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].email", is(equalTo("tina.testing@skoop.io"))))
				.andExpect(jsonPath("$[0].authorizedUsers[0].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].summary").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].specializations").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].certificates").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[0].languages").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].id", is(equalTo("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].userName", is(equalTo("testbed"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].firstName", is(equalTo("Tabia"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].lastName", is(equalTo("Testbed"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].email", is(equalTo("tabia.testbed@skoop.io"))))
				.andExpect(jsonPath("$[0].authorizedUsers[1].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].summary").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].specializations").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].certificates").doesNotExist())
				.andExpect(jsonPath("$[0].authorizedUsers[1].languages").doesNotExist());
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

		mockMvc.perform(put("/users/db87d46a-e4ca-451a-903b-e8533e0b924b/outbound-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(JwtAuthenticationFactory.withUser(foreigner)))
				.with(csrf()))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

}
