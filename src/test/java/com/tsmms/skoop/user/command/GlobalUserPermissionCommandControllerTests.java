package com.tsmms.skoop.user.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.tsmms.skoop.user.command.ReplaceGlobalUserPermissionListCommand.GlobalUserPermissionEntry;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static com.tsmms.skoop.user.GlobalUserPermissionScope.*;

@WebMvcTest(GlobalUserPermissionCommandController.class)
class GlobalUserPermissionCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private GlobalUserPermissionCommandService globalUserPermissionCommandService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Replaces global permissions.")
	@Test
	void replaceGlobalPermissions() throws Exception {
		final User owner = User.builder()
				.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		given(globalUserPermissionCommandService.replaceGlobalUserPermissions(argThat(allOf(
				isA(ReplaceGlobalUserPermissionListCommand.class),
				hasProperty("ownerId", is("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")),
				hasProperty("globalPermissions", containsInAnyOrder(
						GlobalUserPermissionEntry.builder()
								.scope(READ_USER_PROFILE)
								.build(),
						GlobalUserPermissionEntry.builder()
								.scope(READ_USER_SKILLS)
								.build(),
						GlobalUserPermissionEntry.builder()
								.scope(FIND_AS_COACH)
								.build(),
						GlobalUserPermissionEntry.builder()
								.scope(ALLOW_SALES_TO_USE_PERSONALIZED_PROFILE)
								.build()
						)
				))))).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("123")
								.scope(READ_USER_SKILLS)
								.owner(owner)
								.build(),
						GlobalUserPermission.builder()
								.id("456")
								.scope(FIND_AS_COACH)
								.owner(owner)
								.build(),
						GlobalUserPermission.builder()
								.id("789")
								.scope(READ_USER_PROFILE)
								.owner(owner)
								.build(),
						GlobalUserPermission.builder()
								.id("321")
								.scope(ALLOW_SALES_TO_USE_PERSONALIZED_PROFILE)
								.owner(owner)
								.build()
				)
		);

		final ClassPathResource body = new ClassPathResource("permissions/replace-global-permissions.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/global-permissions")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.length()", is(equalTo(4))))
					.andExpect(jsonPath("$[?(@.id=='123')].scope", hasItem("READ_USER_SKILLS")))
					.andExpect(jsonPath("$[?(@.id=='123')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
					.andExpect(jsonPath("$[?(@.id=='123')].owner.userName", hasItem("johndoe")))
					.andExpect(jsonPath("$[?(@.id=='123')].owner.firstName", hasItem("John")))
					.andExpect(jsonPath("$[?(@.id=='123')].owner.lastName", hasItem("Doe")))
					.andExpect(jsonPath("$[?(@.id=='123')].owner.email", hasItem("john.doe@mail.com")))
					.andExpect(jsonPath("$[?(@.id=='456')].scope", hasItem("FIND_AS_COACH")))
					.andExpect(jsonPath("$[?(@.id=='456')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
					.andExpect(jsonPath("$[?(@.id=='456')].owner.userName", hasItem("johndoe")))
					.andExpect(jsonPath("$[?(@.id=='456')].owner.firstName", hasItem("John")))
					.andExpect(jsonPath("$[?(@.id=='456')].owner.lastName", hasItem("Doe")))
					.andExpect(jsonPath("$[?(@.id=='456')].owner.email", hasItem("john.doe@mail.com")))
					.andExpect(jsonPath("$[?(@.id=='789')].scope", hasItem("READ_USER_PROFILE")))
					.andExpect(jsonPath("$[?(@.id=='789')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
					.andExpect(jsonPath("$[?(@.id=='789')].owner.userName", hasItem("johndoe")))
					.andExpect(jsonPath("$[?(@.id=='789')].owner.firstName", hasItem("John")))
					.andExpect(jsonPath("$[?(@.id=='789')].owner.lastName", hasItem("Doe")))
					.andExpect(jsonPath("$[?(@.id=='789')].owner.email", hasItem("john.doe@mail.com")))
					.andExpect(jsonPath("$[?(@.id=='321')].scope", hasItem("ALLOW_SALES_TO_USE_PERSONALIZED_PROFILE")))
					.andExpect(jsonPath("$[?(@.id=='321')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
					.andExpect(jsonPath("$[?(@.id=='321')].owner.userName", hasItem("johndoe")))
					.andExpect(jsonPath("$[?(@.id=='321')].owner.firstName", hasItem("John")))
					.andExpect(jsonPath("$[?(@.id=='321')].owner.lastName", hasItem("Doe")))
					.andExpect(jsonPath("$[?(@.id=='321')].owner.email", hasItem("john.doe@mail.com")));
		}
	}

	@DisplayName("Not authenticated user cannot replace global permissions.")
	@Test
	void notAuthenticatedUserCannotReplaceGlobalPermissions() throws Exception {
		final ClassPathResource body = new ClassPathResource("permissions/replace-global-permissions.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/global-permissions")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot replace global permissions of other users.")
	@Test
	void userCannotReplaceGlobalPermissionsOfOtherUsers() throws Exception {

		final User owner = User.builder()
				.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		final ClassPathResource body = new ClassPathResource("permissions/replace-global-permissions.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/100b9ce9-730b-4c50-a68f-db003428d4ea/global-permissions")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

}
