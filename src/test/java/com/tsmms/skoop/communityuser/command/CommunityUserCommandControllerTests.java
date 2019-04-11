package com.tsmms.skoop.communityuser.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.command.CommunityUserRegistrationApprovalCommand;
import com.tsmms.skoop.communityuser.registration.command.CommunityUserRegistrationCommandService;
import com.tsmms.skoop.communityuser.registration.query.CommunityUserRegistrationQueryService;
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
import java.util.Optional;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityUserCommandController.class)
class CommunityUserCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityUserCommandService communityUserCommandService;

	@MockBean
	private CommunityQueryService communityQueryService;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private CommunityUserRegistrationQueryService communityUserRegistrationQueryService;

	@MockBean
	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@Test
	@DisplayName("Authenticated user joins the community.")
	void userJoinsCommunity() throws Exception {
		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		final Community community = Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(community));

		given(communityQueryService.hasCommunityManagerRole(tester.getId(),"123")).willReturn(false);

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(tester));

		given(communityUserCommandService.create(community, tester, CommunityRole.MEMBER)).willReturn(
				CommunityUser.builder()
						.id(123L)
				.role(CommunityRole.MEMBER)
				.user(tester)
				.community(community)
				.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.build()
		);

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.role", is(equalTo(CommunityRole.MEMBER.toString()))))
					.andExpect(jsonPath("$.user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("tester"))));
		}
	}

	@DisplayName("NOT_FOUND status is returned when trying to join non existent community.")
	@Test
	void notFoundStatusWhenTryingToJoinNonExistentCommunity() throws Exception {
		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.empty());

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isNotFound());
		}
	}

	@DisplayName("NOT_FOUND status is returned when trying to invite non existent user to join a community.")
	@Test
	void notFoundStatusWhenTryingToInviteNonExistentUser() throws Exception {
		final User tester = User.builder()
				.id("abcdefgh-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.empty());

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isNotFound());
		}
	}

	@DisplayName("FORBIDDEN status code is returned when unauthorized user adds a user to the community.")
	@Test
	void forbiddenStatusWhenUnauthorizedUserAddsUserToCommunity() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build()));

		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(false);

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(tester));

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("FORBIDDEN status code is returned when community manager adds user without pending request to the community.")
	@Test
	void forbiddenStatusWhenCommunityManagerAddsUserWithoutPendingRequestToCommunity() throws Exception {
		final User tester = User.builder()
				.id("abcdefgh-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("anotherTester")
						.build()
		));

		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);

		given(communityUserRegistrationQueryService.getPendingUserRequestToJoinCommunity("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123"))
				.willReturn(Optional.empty());

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("Community manager approves user community registration.")
	@Test
	void communityManagerApprovesUserCommunityRegistration() throws Exception {
		final User tester = User.builder()
				.id("abcdefgh-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("anotherTester")
						.build()
		));

		given(communityQueryService.hasCommunityManagerRole("abcdefgh-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);

		given(communityUserRegistrationQueryService.getPendingUserRequestToJoinCommunity("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123"))
				.willReturn(Optional.of(
						CommunityUserRegistration.builder()
								.id("abc")
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 20, 0))
								.registeredUser(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("anotherTester")
										.build())
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.type(CommunityType.CLOSED)
										.build())
								.approvedByUser(true)
								.approvedByCommunity(false)
								.build()
						)
				);

		given(communityUserRegistrationCommandService.approve(
				CommunityUserRegistration.builder()
						.id("abc")
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20, 0))
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("anotherTester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.build())
						.approvedByUser(true)
						.approvedByCommunity(false)
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build()
		)).willReturn(
				CommunityUserRegistration.builder()
						.id("abc")
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20, 0))
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("anotherTester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.build())
						.approvedByUser(true)
						.approvedByCommunity(true)
						.communityUser(
								CommunityUser.builder()
										.role(CommunityRole.MEMBER)
										.user(User.builder()
												.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
												.userName("anotherTester")
												.build())
										.community(Community.builder()
												.id("123")
												.title("Java User Group")
												.type(CommunityType.CLOSED)
												.build())
										.creationDate(LocalDateTime.of(2019, 1, 21, 10, 0))
										.lastModifiedDate(LocalDateTime.of(2019, 1, 21, 10, 0))
										.id(120L)
										.build()
						)
						.build()
		);

		final ClassPathResource body = new ClassPathResource("community/command/join-community-as-member.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities/123/users")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.role", is(equalTo(CommunityRole.MEMBER.toString()))))
					.andExpect(jsonPath("$.user.id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("anotherTester"))));
		}
	}

	@Test
	@DisplayName("User role is changed.")
	void userRoleIsChanged() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(owner.getId(), "123")).willReturn(true);

		given(communityUserCommandService.update("123", "4f09647e-c7d3-4aa6-ab3d-0faff66b951f", CommunityRole.MANAGER)).willReturn(
				CommunityUser.builder()
				.user(User.builder()
						.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
						.userName("other")
						.build()
				)
				.role(CommunityRole.MANAGER)
				.build()
		);

		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.role", is(equalTo(CommunityRole.MANAGER.toString()))))
					.andExpect(jsonPath("$.user.id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
					.andExpect(jsonPath("$.user.userName", is(equalTo("other"))));
		}
	}

	@Test
	@DisplayName("Tests if a community manager cannot change his own role.")
	void testIfCommunityManagerCannotChangeHisOwnRole() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);

		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2")
					.content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(owner))))
					.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("Not authenticated user cannot change community user role.")
	void notAuthenticatedUserCannotChangeCommunityUserRole() throws Exception {
		final ClassPathResource body = new ClassPathResource("community/command/change-community-user-role.json");

		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/users/4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
					.content(is.readAllBytes())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("User who is not a community manager cannot change community user role.")
	void userWhoIsNotCommunityManagerCannotChangeCommunityUserRole() throws Exception {
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
	@DisplayName("BAD_REQUEST status code is returned when invalid role name is passed.")
	void badRequestStatusCodeIsReturnedWhenInvalidRoleNameIsPassed() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);

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
	@DisplayName("Not authenticated user is not allowed to join the community.")
	void notAuthenticatedUserIsNotAllowedToJoinCommunity() throws Exception {
		mockMvc.perform(post("/communities/123/users"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Authenticated user leaves the community.")
	void authenticatedUserLeavesCommunity() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(owner.getId(), "123")).willReturn(false);

		mockMvc.perform(delete("/communities/123/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Not authenticated user is not allowed to leave the community.")
	void notAuthenticatedUserIsNotAllowedToLeaveCommunity() throws Exception {
		mockMvc.perform(delete("/communities/123/members"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Community manager kicks out a community member.")
	void communityManagerKicksOutCommunityMember() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(owner.getId(), "123")).willReturn(true);

		mockMvc.perform(delete("/communities/123/users/a396d5a8-a6b1-4c71-9498-a16e655dae2e")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Community manager is not allowed to leave a community.")
	void communityManagerIsNotAllowedToLeaveCommunity() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(owner.getId(), "123")).willReturn(true);

		mockMvc.perform(delete("/communities/123/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@DisplayName("FORBIDDEN status when an ordinary user removes another community member.")
	@Test
	void forbiddenStatusWhenOrdinaryUserRemovesAnotherCommunityMember() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(owner.getId(), "123")).willReturn(false);

		mockMvc.perform(delete("/communities/123/users/a396d5a8-a6b1-4c71-9498-a16e655dae2e")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

}
