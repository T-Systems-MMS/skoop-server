package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;

@WebMvcTest(CommunityUserCommandController.class)
class CommunityUserCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityUserCommandService communityUserCommandService;

	@Test
	@DisplayName("Tests if authenticated user joins the community.")
	void testIfUserJoinsCommunity() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityUserCommandService.joinCommunityAsMember("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Community.builder()
				.id("123")
				.title("Java User Group")
				.members(singletonList(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()))
				.build()
		);

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.title", is(equalTo("Java User Group"))))
					.andExpect(jsonPath("$.members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.members[0].userName", is(equalTo("tester"))));
		}
	}

	@Test
	@DisplayName("Tests if request to change community user role is processed.")
	void testIfRequestToChangeCommunityUserRoleIsProcessed() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(securityService.hasCommunityManagerRole(argThat(allOf(
				isA(Jwt.class),
				hasProperty("claims", hasEntry(MYSKILLS_USER_ID, "1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
		)), eq("123"))).willReturn(true);

		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isNotImplemented());
		}
	}

	@Test
	@DisplayName("Tests if not authenticated user cannot change community user role.")
	void testIfNotAuthenticatedUserCannotChangeCommunityUserRole() throws Exception {
		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("Tests if a user who is not a community manager cannot change community user role.")
	void testIfUserWhoIsNotCommunityManagerCannotChangeCommunityUserRole() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("Tests if BAD_REQUEST status code is returned when invalid role name is passed.")
	void testIfBadRequestStatusCodeIsReturnedWhenInvalidRoleNameIsPassed() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(securityService.hasCommunityManagerRole(argThat(allOf(
				isA(Jwt.class),
				hasProperty("claims", hasEntry(MYSKILLS_USER_ID, "1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
		)), eq("123"))).willReturn(true);

		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role-invalid.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if not authenticated user is not allowed to join the community.")
	void testIfNotAuthenticatedUserIsNotAllowedToJoinCommunity() throws Exception {
		mockMvc.perform(post("/communities/123/members"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Tests if authenticated user leaves the community.")
	void testIfAuthenticatedUserLeavesCommunity() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityUserCommandService.leaveCommunity("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Community.builder()
				.id("123")
				.title("Java User Group")
				.members(singletonList(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("anotherTester")
						.build()))
				.build()
		);

		mockMvc.perform(delete("/communities/123/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$.members").doesNotExist());
	}

	@Test
	@DisplayName("Tests if not authenticated user is not allowed to leave the community.")
	void testIfNotAuthenticatedUserIsNotAllowedToLeaveCommunity() throws Exception {
		mockMvc.perform(delete("/communities/123/members"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Test if community manager kicks out a community member.")
	void testIfCommunityManagesKicksOutCommunityMember() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(securityService.hasCommunityManagerRole(argThat(allOf(
				isA(Jwt.class),
				hasProperty("claims", hasEntry(MYSKILLS_USER_ID, "1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
		)), eq("123"))).willReturn(true);

		given(communityUserCommandService.leaveCommunity("123", "a396d5a8-a6b1-4c71-9498-a16e655dae2e")).willReturn(Community.builder()
				.id("123")
				.title("Java User Group")
				.members(singletonList(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("anotherTester")
						.build()))
				.build()
		);

		mockMvc.perform(delete("/communities/123/users/a396d5a8-a6b1-4c71-9498-a16e655dae2e")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$.members[0].id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$.members[0].userName", is(equalTo("anotherTester"))));
	}

}
