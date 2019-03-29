package io.knowledgeassets.myskills.server.user.notification;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.notification.kickout.UserKickedOutFromCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.notification.leaving.UserLeftCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.notification.invitation.InvitationToJoinCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.registration.notification.request.RequestToJoinCommunityNotification;
import io.knowledgeassets.myskills.server.notification.query.NotificationQueryService;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserNotificationQueryController.class)
class UserNotificationQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificationQueryService notificationQueryService;

	@DisplayName("Gets user notifications.")
	@Test
	void getUserNotifications() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(notificationQueryService.getUserNotifications("56ef4778-a084-4509-9a3e-80b7895cf7b0")).willReturn(Stream.of(
				InvitationToJoinCommunityNotification.builder()
						.id("123")
						.creationDatetime(LocalDateTime.of(2019, 3, 27, 9, 34))
						.registration(CommunityUserRegistration.builder()
								.approvedByUser(null)
								.approvedByCommunity(true)
								.registeredUser(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
								.community(Community.builder()
										.id("836713dc-a10d-4c85-8613-abe0fa0cf210")
										.type(CommunityType.CLOSED)
										.title("Community")
										.build()
								)
								.build())
						.build(),
				RequestToJoinCommunityNotification.builder()
						.id("456")
						.creationDatetime(LocalDateTime.of(2019, 3, 27, 10, 34))
						.registration(CommunityUserRegistration.builder()
								.approvedByUser(true)
								.approvedByCommunity(null)
								.registeredUser(User.builder()
										.id("47fd5fb1-ccf5-4163-986f-6eeb9ef37280")
										.userName("anotherTester")
										.build())
								.community(Community.builder()
										.id("7d616eaa-8a09-420a-b4f2-99b74b360308")
										.type(CommunityType.CLOSED)
										.title("AnotherCommunity")
										.build()
								)
								.build())
						.build(),
				UserKickedOutFromCommunityNotification.builder()
						.id("789")
						.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
						.user(tester)
						.community(Community.builder()
								.id("773e8cce-fc06-4a62-a23e-58b52c097600")
								.title("Community the user was kicked out from")
								.type(CommunityType.CLOSED)
								.build()
						)
						.build(),
				UserLeftCommunityNotification.builder()
						.id("901")
						.creationDatetime(LocalDateTime.of(2019, 3, 21, 15, 30))
						.community(Community.builder()
								.id("7d616eaa-8a09-420a-b4f2-99b74b360308")
								.type(CommunityType.CLOSED)
								.title("AnotherCommunity")
								.build())
						.user(User.builder()
								.id("fddd5bdd-8931-4eb6-a875-b52e10e07d35")
								.userName("UserLeftCommunity")
								.build())
						.build()
		));

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/notifications")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(4))))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].type", is(equalTo("InvitationToJoinCommunityNotificationResponse"))))
				.andExpect(jsonPath("$[0].creationDatetime", is(equalTo("2019-03-27T09:34:00"))))
				.andExpect(jsonPath("$[0].registration.approvedByUser", nullValue()))
				.andExpect(jsonPath("$[0].registration.approvedByCommunity", is(equalTo(true))))
				.andExpect(jsonPath("$[0].registration.user.id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$[0].registration.user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].registration.community.id", is(equalTo("836713dc-a10d-4c85-8613-abe0fa0cf210"))))
				.andExpect(jsonPath("$[0].registration.community.type", is(equalTo("CLOSED"))))
				.andExpect(jsonPath("$[0].registration.community.title", is(equalTo("Community"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].type", is(equalTo("RequestToJoinCommunityNotificationResponse"))))
				.andExpect(jsonPath("$[1].creationDatetime", is(equalTo("2019-03-27T10:34:00"))))
				.andExpect(jsonPath("$[1].registration.approvedByUser", is(equalTo(true))))
				.andExpect(jsonPath("$[1].registration.approvedByCommunity", nullValue()))
				.andExpect(jsonPath("$[1].registration.user.id", is(equalTo("47fd5fb1-ccf5-4163-986f-6eeb9ef37280"))))
				.andExpect(jsonPath("$[1].registration.user.userName", is(equalTo("anotherTester"))))
				.andExpect(jsonPath("$[1].registration.community.id", is(equalTo("7d616eaa-8a09-420a-b4f2-99b74b360308"))))
				.andExpect(jsonPath("$[1].registration.community.type", is(equalTo("CLOSED"))))
				.andExpect(jsonPath("$[1].registration.community.title", is(equalTo("AnotherCommunity"))))
				.andExpect(jsonPath("$[2].id", is(equalTo("789"))))
				.andExpect(jsonPath("$[2].type", is(equalTo("UserKickedOutFromCommunityNotificationResponse"))))
				.andExpect(jsonPath("$[2].creationDatetime", is(equalTo("2019-03-25T10:00:00"))))
				.andExpect(jsonPath("$[2].user.id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$[2].user.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[2].community.id", is(equalTo("773e8cce-fc06-4a62-a23e-58b52c097600"))))
				.andExpect(jsonPath("$[2].community.title", is(equalTo("Community the user was kicked out from"))))
				.andExpect(jsonPath("$[2].community.type", is(equalTo("CLOSED"))))
				.andExpect(jsonPath("$[3].id", is(equalTo("901"))))
				.andExpect(jsonPath("$[3].type", is(equalTo("UserLeftCommunityNotificationResponse"))))
				.andExpect(jsonPath("$[3].creationDatetime", is(equalTo("2019-03-21T15:30:00"))))
				.andExpect(jsonPath("$[3].user.id", is(equalTo("fddd5bdd-8931-4eb6-a875-b52e10e07d35"))))
				.andExpect(jsonPath("$[3].user.userName", is(equalTo("UserLeftCommunity"))))
				.andExpect(jsonPath("$[3].community.id", is(equalTo("7d616eaa-8a09-420a-b4f2-99b74b360308"))))
				.andExpect(jsonPath("$[3].community.title", is(equalTo("AnotherCommunity"))))
				.andExpect(jsonPath("$[3].community.type", is(equalTo("CLOSED"))));
	}

	@DisplayName("Not authenticated user cannot get notifications.")
	@Test
	void notAuthenticatedUserCannotGetNotifications() throws Exception {
		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/notifications")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("A user cannot get notifictions of another user.")
	@Test
	void userCannotGetNotificationsOfAnotherUser() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(get("/users/47fd5fb1-ccf5-4163-986f-6eeb9ef37280/notifications")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester))))
				.andExpect(status().isForbidden());
	}

}
