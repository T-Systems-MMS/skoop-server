package com.tsmms.skoop.notification.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.communityuser.registration.AcceptanceToCommunityNotification;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import com.tsmms.skoop.communityuser.registration.InvitationToJoinCommunityNotification;
import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.NotificationRepository;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceTests {

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private CommunityUserRegistrationRepository communityUserRegistrationRepository;

	private NotificationCommandService notificationCommandService;

	@BeforeEach
	void setUp() {
		this.notificationCommandService = new NotificationCommandService(notificationRepository, communityUserRegistrationRepository);
	}

	@DisplayName("Saves a notification.")
	@Test
	void savesNotification() {
		given(notificationRepository.save(InvitationToJoinCommunityNotification.builder()
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
		)).willReturn(InvitationToJoinCommunityNotification.builder()
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
				.build());

		final Notification notification = notificationCommandService.save(InvitationToJoinCommunityNotification.builder()
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
				.build());

		assertThat(notification.getId()).isEqualTo("123");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 27, 9, 34));
		assertThat(notification).isInstanceOf(InvitationToJoinCommunityNotification.class);
		final InvitationToJoinCommunityNotification invitationToJoinCommunityNotification = (InvitationToJoinCommunityNotification) notification;
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByUser()).isNull();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByCommunity()).isTrue();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getRegisteredUser().getId()).isEqualTo("abc");
		assertThat(invitationToJoinCommunityNotification.getRegistration().getRegisteredUser().getUserName()).isEqualTo("tester");
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCommunity().getId()).isEqualTo("cba");
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCommunity().getType()).isEqualTo(CommunityType.CLOSED);
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCommunity().getTitle()).isEqualTo("Community");
		assertThat(invitationToJoinCommunityNotification.getCommunityName()).isEqualTo("Community");
	}

	@DisplayName("Deletes notification.")
	@Test
	void deleteNotification() {
		assertDoesNotThrow(() -> notificationCommandService.delete(
				AcceptanceToCommunityNotification.builder()
						.id("123")
				.communityName("Community")
				.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
				.registration(
						CommunityUserRegistration.builder()
								.approvedByUser(true)
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
								.build()
				)
				.build()
		));
	}

	@DisplayName("Exception is thrown if null is passed as ID when deleting notification.")
	@Test
	void exceptionIsThrownIfNullIsPassedWhenDeletingNotification() {
		assertThrows(IllegalArgumentException.class, () -> notificationCommandService.delete(null));
	}

	@DisplayName("Deletes notifications.")
	@Test
	void deleteNotifications() {
		assertDoesNotThrow(() -> notificationCommandService.deleteAll(Collections.singletonList(
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
		)));
	}

}
