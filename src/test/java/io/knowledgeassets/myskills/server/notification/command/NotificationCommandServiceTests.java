package io.knowledgeassets.myskills.server.notification.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.notification.invitation.InvitationToJoinCommunityNotification;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationRepository;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceTests {

	@Mock
	private NotificationRepository notificationRepository;

	private NotificationCommandService notificationCommandService;

	@BeforeEach
	void setUp() {
		this.notificationCommandService = new NotificationCommandService(notificationRepository);
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
				.build());

		final Notification notification = this.notificationCommandService.save(InvitationToJoinCommunityNotification.builder()
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
	}

}
