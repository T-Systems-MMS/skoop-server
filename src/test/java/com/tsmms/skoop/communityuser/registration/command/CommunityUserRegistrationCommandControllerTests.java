package com.tsmms.skoop.communityuser.registration.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.command.CommunityUserCommandService;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityUserRegistrationCommandController.class)
class CommunityUserRegistrationCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private CommunityQueryService communityQueryService;

	@MockBean
	private CommunityUserRegistrationQueryService communityUserRegistrationQueryService;

	@MockBean
	private CommunityUserCommandService communityUserCommandService;

	@DisplayName("Create user registration on behalf of community manager.")
	@Test
	void createUserRegistrationsOnBehalfOfCommunity() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUsersByIds(Arrays.asList("abc", "def"))).willReturn(Stream.of(User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));

		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(true);

		given(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Arrays.asList(
				User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build())).willReturn(Arrays.asList(CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("abc")
								.userName("firstUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build(),
				CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("def")
								.userName("secondUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build()
		));

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registrations.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("firstUser"))))
					.andExpect(jsonPath("$[0].approvedByUser", nullValue()))
					.andExpect(jsonPath("$[0].approvedByCommunity", is(equalTo(true))))
					.andExpect(jsonPath("$[1].user.userName", is(equalTo("secondUser"))))
					.andExpect(jsonPath("$[1].approvedByUser", nullValue()))
					.andExpect(jsonPath("$[1].approvedByCommunity", is(equalTo(true))));
		}
	}

	@DisplayName("Create user registration on behalf of user.")
	@Test
	void createUserRegistrationOnBehalfOfUser() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUsersByIds(Collections.singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));

		given(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfUser(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build(), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build())
		).willReturn(CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.approvedByCommunity(null)
						.approvedByUser(true)
						.build()
		);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registration.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$[0].approvedByUser", is(equalTo(true))))
					.andExpect(jsonPath("$[0].approvedByCommunity", nullValue()));
		}

	}

	@DisplayName("Create user registration on behalf of user to join open community.")
	@Test
	void createUserRegistrationOnBehalfOfUserToJoinOpenCommunity() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUsersByIds(Collections.singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build()
		));

		given(communityUserCommandService.create(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build(),
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				CommunityRole.MEMBER
		)).willReturn(CommunityUser.builder()
				.id(123L)
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 19, 0))
				.creationDate(LocalDateTime.of(2019, 1, 20, 19, 0))
				.community(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build())
				.user(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build())
				.role(CommunityRole.MEMBER)
				.build()
		);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registration.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$[0].approvedByUser", is(equalTo(true))))
					.andExpect(jsonPath("$[0].approvedByCommunity", is(equalTo(true))));
		}

	}

	@DisplayName("The community member invites users to join an open community.")
	@Test
	void communityMemberInvitesUsersToJoinOpenCommunity() throws Exception {
		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.isCommunityMember(tester.getId(), "123")).willReturn(true);
		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(false);

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build()
		));

		given(userQueryService.getUsersByIds(Arrays.asList("abc", "def"))).willReturn(Stream.of(User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		));

		given(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Arrays.asList(
				User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.build())).willReturn(Arrays.asList(CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("abc")
								.userName("firstUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build(),
				CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("def")
								.userName("secondUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build()
		));

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registrations.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("firstUser"))))
					.andExpect(jsonPath("$[0].approvedByUser", nullValue()))
					.andExpect(jsonPath("$[0].approvedByCommunity", is(equalTo(true))))
					.andExpect(jsonPath("$[1].user.userName", is(equalTo("secondUser"))))
					.andExpect(jsonPath("$[1].approvedByUser", nullValue()))
					.andExpect(jsonPath("$[1].approvedByCommunity", is(equalTo(true))));
		}
	}

	@DisplayName("The community member cannot invite himself to join an open community.")
	@Test
	void communityMemberCannotInviteHimselfToJoinOpenCommunity() throws Exception {
		final User tester = User.builder()
				.id("abc")
				.userName("firstUser")
				.build();

		given(communityQueryService.isCommunityMember(tester.getId(), "123")).willReturn(true);
		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(false);

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build()
		));

		given(userQueryService.getUsersByIds(Arrays.asList("abc", "def"))).willReturn(Stream.of(User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		));

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registrations.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("NOT_FOUND status code is returned when community user registration is created for non existent community.")
	@Test
	void notFoundStatusIsReturnedWhenCommunityUserRegistrationIsCreatedForNonExistentCommunity() throws Exception {
		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUsersByIds(Arrays.asList("abc", "def"))).willReturn(Stream.of(User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.empty());

		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(true);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registrations.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isNotFound());
		}
	}

	@DisplayName("FORBIDDEN status code is returned when user is not authenticated to create community user registration.")
	@Test
	void forbiddenStatusIsReturnedWhenUserNotAuthenticatedToCreateCommunityUserRegistration() throws Exception {
		final User tester = User.builder()
				.id("abcdefgh-b4d0-4119-9113-4677beb20ae2")
				.userName("anotherTester")
				.build();

		given(userQueryService.getUsersByIds(Collections.singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.build()
		));

		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(false);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registration.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("User registration is updated on behalf of user.")
	@Test
	void userRegistrationIsUpdatedOnBehalfOfUser() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityUserRegistrationQueryService.getCommunityUserRegistrationById("456")).willReturn(
				Optional.of(
						CommunityUserRegistration.builder()
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.build()
								)
								.registeredUser(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build())
								.approvedByCommunity(true)
								.approvedByUser(null)
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
								.id("456")
								.build()
				)
		);

		given(communityUserRegistrationCommandService.approve(
				CommunityUserRegistration.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build()
						)
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
						.approvedByCommunity(true)
						.approvedByUser(null)
						.id("456")
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
				.approvedByUser(true)
				.approvedByCommunity(null)
				.build()
		)).willReturn(
				CommunityUserRegistration.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build()
						)
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(true)
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
						.id("456")
						.build()
		);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/update-user-registration.json");
		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/user-registrations/456").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.approvedByUser", is(equalTo(true))))
					.andExpect(jsonPath("$.approvedByCommunity", is(equalTo(true))));
		}
	}

	@DisplayName("User registration is updated on behalf of community manager.")
	@Test
	void userRegistrationIsUpdatedOnBehalfOfCommunityManager() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityUserRegistrationQueryService.getCommunityUserRegistrationById("456")).willReturn(
				Optional.of(
						CommunityUserRegistration.builder()
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.build()
								)
								.registeredUser(User.builder()
										.id("hd84ba8kju-b4d0-4119-9113-4677beb20ae2")
										.userName("anotherTester")
										.build())
								.approvedByCommunity(null)
								.approvedByUser(true)
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
								.id("456")
								.build()
				)
		);

		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(true);

		given(communityUserRegistrationCommandService.approve(
				CommunityUserRegistration.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build()
						)
						.registeredUser(User.builder()
								.id("hd84ba8kju-b4d0-4119-9113-4677beb20ae2")
								.userName("anotherTester")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
						.approvedByCommunity(null)
						.approvedByUser(true)
						.id("456")
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
						.approvedByUser(null)
						.approvedByCommunity(true)
						.build()
		)).willReturn(
				CommunityUserRegistration.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build()
						)
						.registeredUser(User.builder()
								.id("hd84ba8kju-b4d0-4119-9113-4677beb20ae2")
								.userName("anotherTester")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(true)
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
						.id("456")
						.build()
		);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/update-user-registration.json");
		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/user-registrations/456").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.user.userName", is(equalTo("anotherTester"))))
					.andExpect(jsonPath("$.approvedByUser", is(equalTo(true))))
					.andExpect(jsonPath("$.approvedByCommunity", is(equalTo(true))));
		}
	}

	@DisplayName("NOT_FOUND status is returned when non-existent community user registration is updated.")
	@Test
	void notFoundStatusIsReturnedWhenNonExistentCommunityUserRegistrationIsUpdated() throws Exception {
		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(communityUserRegistrationQueryService.getCommunityUserRegistrationById("456")).willReturn(Optional.empty());
		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/update-user-registration.json");
		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/user-registrations/456").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isNotFound());
		}
	}

	@DisplayName("FORBIDDEN status code is returned when the user is not authorized to updated community user registration.")
	@Test
	void forbiddenStatusCodeIsReturnedWhenUserIsNotAuthenticatedToUpdateCommunityUserRegistration() throws Exception {
		given(communityUserRegistrationQueryService.getCommunityUserRegistrationById("456")).willReturn(
				Optional.of(
						CommunityUserRegistration.builder()
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.build()
								)
								.registeredUser(User.builder()
										.id("hd84ba8kju-b4d0-4119-9113-4677beb20ae2")
										.userName("anotherTester")
										.build())
								.approvedByCommunity(false)
								.approvedByUser(true)
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 20,0))
								.id("456")
								.build()
				)
		);

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.hasCommunityManagerRole(tester.getId(), "123")).willReturn(false);

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/update-user-registration.json");
		try (InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123/user-registrations/456").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isForbidden());
		}
	}

}
