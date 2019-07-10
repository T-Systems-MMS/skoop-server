package com.tsmms.skoop.notification.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.communityuser.UserKickedOutFromCommunityNotification;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.InvitationToJoinCommunityNotification;
import com.tsmms.skoop.communityuser.registration.RequestToJoinCommunityNotification;
import com.tsmms.skoop.notification.query.NotificationQueryService;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;

@WebMvcTest(NotificationCommandController.class)
class NotificationCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private NotificationCommandService notificationCommandService;

	@MockBean
	private NotificationQueryService notificationQueryService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Deletes notification.")
	@Test
	void deleteNotification() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		given(notificationQueryService.getNotification("789")).willReturn(
				Optional.of(
						UserKickedOutFromCommunityNotification.builder()
								.id("789")
								.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
								.user(User.builder()
										.id("abc")
										.userName("tester")
										.build())
								.community(Community.builder()
										.id("145")
										.title("JavaScript User Group")
										.type(CommunityType.CLOSED)
										.build()
								)
								.communityName("JavaScript User Group")
								.build()
				)
		);
		given(notificationQueryService.getUserNotifications(tester.getId())).willReturn(
			Stream.of(
					InvitationToJoinCommunityNotification.builder()
							.id("123")
							.creationDatetime(LocalDateTime.of(2019, 3, 27, 9, 34))
							.registration(CommunityUserRegistration.builder()
									.approvedByUser(null)
									.approvedByCommunity(true)
									.registeredUser(User.builder()
											.id("abc")
											.userName("tester")
											.build())
									.community(Community.builder()
											.id("cba")
											.type(CommunityType.CLOSED)
											.title("Community")
											.build()
									)
									.build())
							.communityName("Community")
							.build(),
					RequestToJoinCommunityNotification.builder()
							.id("456")
							.creationDatetime(LocalDateTime.of(2019, 3, 27, 10, 34))
							.registration(CommunityUserRegistration.builder()
									.approvedByUser(true)
									.approvedByCommunity(null)
									.registeredUser(User.builder()
											.id("def")
											.userName("anotherTester")
											.build())
									.community(Community.builder()
											.id("xyz")
											.type(CommunityType.CLOSED)
											.title("AnotherCommunity")
											.build()
									)
									.build())
							.communityName("AnotherCommunity")
							.build(),
					UserKickedOutFromCommunityNotification.builder()
							.id("789")
							.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
							.user(User.builder()
									.id("abc")
									.userName("tester")
									.build())
							.community(Community.builder()
									.id("145")
									.title("JavaScript User Group")
									.type(CommunityType.CLOSED)
									.build()
							)
							.communityName("JavaScript User Group")
							.build()
			)
		);
		mockMvc.perform(delete("/notifications/789")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@DisplayName("Notification cannot be deleted by not owning user.")
	@Test
	void notificationCannotBeDeletedByNotOwningUser() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		given(notificationQueryService.getNotification("789")).willReturn(
				Optional.of(
						UserKickedOutFromCommunityNotification.builder()
								.id("789")
								.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
								.user(User.builder()
										.id("abc")
										.userName("tester")
										.build())
								.community(Community.builder()
										.id("145")
										.title("JavaScript User Group")
										.type(CommunityType.CLOSED)
										.build()
								)
								.communityName("JavaScript User Group")
								.build()
				)
		);
		given(notificationQueryService.getUserNotifications(tester.getId())).willReturn(
				Stream.of(
						InvitationToJoinCommunityNotification.builder()
								.id("123")
								.creationDatetime(LocalDateTime.of(2019, 3, 27, 9, 34))
								.registration(CommunityUserRegistration.builder()
										.approvedByUser(null)
										.approvedByCommunity(true)
										.registeredUser(User.builder()
												.id("abc")
												.userName("tester")
												.build())
										.community(Community.builder()
												.id("cba")
												.type(CommunityType.CLOSED)
												.title("Community")
												.build()
										)
										.build())
								.communityName("Community")
								.build(),
						RequestToJoinCommunityNotification.builder()
								.id("456")
								.creationDatetime(LocalDateTime.of(2019, 3, 27, 10, 34))
								.registration(CommunityUserRegistration.builder()
										.approvedByUser(true)
										.approvedByCommunity(null)
										.registeredUser(User.builder()
												.id("def")
												.userName("anotherTester")
												.build())
										.community(Community.builder()
												.id("xyz")
												.type(CommunityType.CLOSED)
												.title("AnotherCommunity")
												.build()
										)
										.build())
								.communityName("AnotherCommunity")
								.build()
				)
		);
		mockMvc.perform(delete("/notifications/789")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@DisplayName("To-do cannot be deleted.")
	@Test
	void toDoCannotBeDeleted() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		given(notificationQueryService.getNotification("123")).willReturn(
				Optional.of(
						InvitationToJoinCommunityNotification.builder()
								.id("123")
								.creationDatetime(LocalDateTime.of(2019, 3, 27, 9, 34))
								.registration(CommunityUserRegistration.builder()
										.approvedByUser(null)
										.approvedByCommunity(true)
										.registeredUser(User.builder()
												.id("abc")
												.userName("tester")
												.build())
										.community(Community.builder()
												.id("cba")
												.type(CommunityType.CLOSED)
												.title("Community")
												.build()
										)
										.build())
								.communityName("Community")
								.build()
				)
		);
		given(notificationQueryService.getUserNotifications(tester.getId())).willReturn(
				Stream.of(
						InvitationToJoinCommunityNotification.builder()
								.id("123")
								.creationDatetime(LocalDateTime.of(2019, 3, 27, 9, 34))
								.registration(CommunityUserRegistration.builder()
										.approvedByUser(null)
										.approvedByCommunity(true)
										.registeredUser(User.builder()
												.id("abc")
												.userName("tester")
												.build())
										.community(Community.builder()
												.id("cba")
												.type(CommunityType.CLOSED)
												.title("Community")
												.build()
										)
										.build())
								.communityName("Community")
								.build(),
						RequestToJoinCommunityNotification.builder()
								.id("456")
								.creationDatetime(LocalDateTime.of(2019, 3, 27, 10, 34))
								.registration(CommunityUserRegistration.builder()
										.approvedByUser(true)
										.approvedByCommunity(null)
										.registeredUser(User.builder()
												.id("def")
												.userName("anotherTester")
												.build())
										.community(Community.builder()
												.id("xyz")
												.type(CommunityType.CLOSED)
												.title("AnotherCommunity")
												.build()
										)
										.build())
								.communityName("AnotherCommunity")
								.build(),
						UserKickedOutFromCommunityNotification.builder()
								.id("789")
								.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
								.user(User.builder()
										.id("abc")
										.userName("tester")
										.build())
								.community(Community.builder()
										.id("145")
										.title("JavaScript User Group")
										.type(CommunityType.CLOSED)
										.build()
								)
								.communityName("JavaScript User Group")
								.build()
				)
		);
		mockMvc.perform(delete("/notifications/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@DisplayName("Deleted notification cannot be found.")
	@Test
	void deletedNotificationCannotBeFound() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		given(notificationQueryService.getNotification("123")).willReturn(Optional.empty());
		mockMvc.perform(delete("/notifications/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isNotFound());
	}

	@DisplayName("Unauthenticated user cannot delete notification.")
	@Test
	void unauthenticatedUserCannotDeleteNotification() throws Exception {
		given(notificationQueryService.getNotification("123")).willReturn(Optional.empty());
		mockMvc.perform(delete("/notifications/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

}
