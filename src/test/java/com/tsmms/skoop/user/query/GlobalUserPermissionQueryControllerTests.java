package com.tsmms.skoop.user.query;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static com.tsmms.skoop.user.GlobalUserPermissionScope.*;

@WebMvcTest(GlobalUserPermissionQueryController.class)
class GlobalUserPermissionQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Gets outbound global user permissions.")
	@Test
	void getOutboundGlobalUserPermissions() throws Exception {

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

		given(globalUserPermissionQueryService.getOutboundGlobalUserPermissions("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("123")
								.scope(READ_USER_PROFILE)
								.owner(owner)
								.build(),
						GlobalUserPermission.builder()
								.id("456")
								.scope(FIND_AS_COACH)
								.owner(owner)
								.build(),
						GlobalUserPermission.builder()
								.id("789")
								.scope(READ_USER_SKILLS)
								.owner(owner)
								.build()
				)
		);

		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/outbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(3))))
				.andExpect(jsonPath("$[?(@.id=='123')].scope", hasItem("READ_USER_PROFILE")))
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
				.andExpect(jsonPath("$[?(@.id=='789')].scope", hasItem("READ_USER_SKILLS")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.userName", hasItem("johndoe")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.firstName", hasItem("John")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.lastName", hasItem("Doe")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.email", hasItem("john.doe@mail.com")));
	}

	@DisplayName("Gets outbound global user permissions by scope.")
	@Test
	void getOutboundGlobalUserPermissionsByScope() throws Exception {

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

		given(globalUserPermissionQueryService.getOutboundGlobalUserPermissionsByScope("adac977c-8e0d-4e00-98a8-da7b44aa5dd6", READ_USER_SKILLS)).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("789")
								.scope(READ_USER_SKILLS)
								.owner(owner)
								.build()
				)
		);

		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/outbound-global-permissions")
				.param("scope", "READ_USER_SKILLS")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[?(@.id=='789')].scope", hasItem("READ_USER_SKILLS")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.id", hasItem("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.userName", hasItem("johndoe")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.firstName", hasItem("John")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.lastName", hasItem("Doe")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.email", hasItem("john.doe@mail.com")));
	}

	@DisplayName("Not authenticated user cannot get outbound global permissions.")
	@Test
	void notAuthenticatedUserCannotGetOutboundGlobalPermissions() throws Exception {
		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/outbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot get outbound global permissions of other users.")
	@Test
	void userCannotGetOutboundGlobalPermissionsOfOtherUsers() throws Exception {
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

		mockMvc.perform(get("/users/100b9ce9-730b-4c50-a68f-db003428d4ea/outbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@DisplayName("Gets inbound global user permissions.")
	@Test
	void getInboundGlobalUserPermissions() throws Exception {

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

		given(globalUserPermissionQueryService.getInboundGlobalUserPermissions("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("123")
								.scope(READ_USER_PROFILE)
								.owner(User.builder()
										.id("abc")
										.userName("tester")
										.firstName("test")
										.lastName("test")
										.email("test@skoop.io")
										.build())
								.build(),
						GlobalUserPermission.builder()
								.id("456")
								.scope(FIND_AS_COACH)
								.owner(User.builder()
										.id("abc")
										.userName("tester")
										.firstName("test")
										.lastName("test")
										.email("test@skoop.io")
										.build())
								.build(),
						GlobalUserPermission.builder()
								.id("789")
								.scope(READ_USER_SKILLS)
								.owner(User.builder()
										.id("abc")
										.userName("tester")
										.firstName("test")
										.lastName("test")
										.email("test@skoop.io")
										.build())
								.build()
				)
		);

		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(3))))
				.andExpect(jsonPath("$[?(@.id=='123')].scope", hasItem("READ_USER_PROFILE")))
				.andExpect(jsonPath("$[?(@.id=='123')].owner.id", hasItem("abc")))
				.andExpect(jsonPath("$[?(@.id=='123')].owner.userName", hasItem("tester")))
				.andExpect(jsonPath("$[?(@.id=='123')].owner.firstName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='123')].owner.lastName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='123')].owner.email", hasItem("test@skoop.io")))
				.andExpect(jsonPath("$[?(@.id=='456')].scope", hasItem("FIND_AS_COACH")))
				.andExpect(jsonPath("$[?(@.id=='456')].owner.id", hasItem("abc")))
				.andExpect(jsonPath("$[?(@.id=='456')].owner.userName", hasItem("tester")))
				.andExpect(jsonPath("$[?(@.id=='456')].owner.firstName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='456')].owner.lastName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='456')].owner.email", hasItem("test@skoop.io")))
				.andExpect(jsonPath("$[?(@.id=='789')].scope", hasItem("READ_USER_SKILLS")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.id", hasItem("abc")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.userName", hasItem("tester")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.firstName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.lastName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.email", hasItem("test@skoop.io")));
	}

	@DisplayName("Gets inbound global user permissions by scope.")
	@Test
	void getInboundGlobalUserPermissionsByScope() throws Exception {

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

		given(globalUserPermissionQueryService.getInboundGlobalUserPermissionsByScope("adac977c-8e0d-4e00-98a8-da7b44aa5dd6", READ_USER_SKILLS)).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("789")
								.scope(READ_USER_SKILLS)
								.owner(User.builder()
										.id("abc")
										.userName("tester")
										.firstName("test")
										.lastName("test")
										.email("test@skoop.io")
										.build())
								.build()
				)
		);

		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-global-permissions")
				.param("scope", "READ_USER_SKILLS")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[?(@.id=='789')].scope", hasItem("READ_USER_SKILLS")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.id", hasItem("abc")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.userName", hasItem("tester")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.firstName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.lastName", hasItem("test")))
				.andExpect(jsonPath("$[?(@.id=='789')].owner.email", hasItem("test@skoop.io")));
	}

	@DisplayName("Not authenticated user cannot get inbound global permissions.")
	@Test
	void notAuthenticatedUserCannotGetInboundGlobalPermissions() throws Exception {
		mockMvc.perform(get("/users/adac977c-8e0d-4e00-98a8-da7b44aa5dd6/inbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot get inbound global permissions of other users.")
	@Test
	void userCannotGetInboundGlobalPermissionsOfOtherUsers() throws Exception {
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

		mockMvc.perform(get("/users/100b9ce9-730b-4c50-a68f-db003428d4ea/inbound-global-permissions")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

}
