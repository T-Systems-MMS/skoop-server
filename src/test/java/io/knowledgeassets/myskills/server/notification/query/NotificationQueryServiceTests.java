package io.knowledgeassets.myskills.server.notification.query;


import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationRepository;
import io.knowledgeassets.myskills.server.notification.NotificationType;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.stream.Stream;

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
				Notification.builder()
						.id("123")
						.type(NotificationType.INVITATION_TO_JOIN_COMMUNITY)
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
				Notification.builder()
						.id("456")
						.type(NotificationType.REQUEST_TO_JOIN_COMMUNITY)
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
						.build()
		));

		final Stream<Notification> notificationStream = this.notificationQueryService.getUserNotifications("abc");

		assertThat(notificationStream).containsExactly(
				Notification.builder()
						.id("123")
						.type(NotificationType.INVITATION_TO_JOIN_COMMUNITY)
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
				Notification.builder()
						.id("456")
						.type(NotificationType.REQUEST_TO_JOIN_COMMUNITY)
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
						.build()
		);
	}

}
