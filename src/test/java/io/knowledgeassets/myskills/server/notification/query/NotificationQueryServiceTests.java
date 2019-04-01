package io.knowledgeassets.myskills.server.notification.query;


import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityDeletedNotification;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.UserKickedOutFromCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.UserLeftCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.InvitationToJoinCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.registration.RequestToJoinCommunityNotification;
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
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceTests {

	@Mock
	private NotificationRepository notificationRepository;

	private NotificationQueryService notificationQueryService;

	@BeforeEach
	void setUp() {
		this.notificationQueryService = new NotificationQueryService(notificationRepository);
	}

	@DisplayName("Gets user notifications.")
	@Test
	void getsUserNotifications() {
		given(notificationRepository.getUserNotifications("abc")).willReturn(Stream.of(
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
						.build(),
				UserKickedOutFromCommunityNotification.builder()
						.id("def")
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
						.build(),
				UserLeftCommunityNotification.builder()
						.id("zyx")
						.creationDatetime(LocalDateTime.of(2019, 3, 21, 15, 30))
						.community(Community.builder()
								.id("xyz")
								.type(CommunityType.CLOSED)
								.title("AnotherCommunity")
								.build())
						.user(User.builder()
								.id("0123")
								.userName("UserLeftCommunity")
								.build())
						.build(),
				CommunityDeletedNotification.builder()
						.id("iop")
						.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
						.communityName("Deleted community")
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.build()
		));

		final Stream<Notification> notificationStream = this.notificationQueryService.getUserNotifications("abc");

		assertThat(notificationStream).containsExactly(
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
						.build(),
				UserKickedOutFromCommunityNotification.builder()
						.id("def")
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
						.build(),
				UserLeftCommunityNotification.builder()
						.id("zyx")
						.creationDatetime(LocalDateTime.of(2019, 3, 21, 15, 30))
						.community(Community.builder()
								.id("xyz")
								.type(CommunityType.CLOSED)
								.title("AnotherCommunity")
								.build())
						.user(User.builder()
								.id("0123")
								.userName("UserLeftCommunity")
								.build())
						.build(),
				CommunityDeletedNotification.builder()
						.id("iop")
						.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
						.communityName("Deleted community")
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.build()
		);
	}

}
