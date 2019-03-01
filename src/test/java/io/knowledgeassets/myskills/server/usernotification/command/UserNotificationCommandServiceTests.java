package io.knowledgeassets.myskills.server.usernotification.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.usernotification.UserNotification;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationRepository;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationStatus;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class UserNotificationCommandServiceTests {

	@Mock
	private UserNotificationRepository userNotificationRepository;

	@Mock
	private CurrentUserService currentUserService;

	private UserNotificationCommandService userNotificationCommandService;

	@BeforeEach
	void setUp() {
		this.userNotificationCommandService = new UserNotificationCommandService(userNotificationRepository,
				currentUserService);
	}

	@DisplayName("Test if users are invited.")
	@Test
	void testIfUsersAreInvited() {

		given(currentUserService.getCurrentUser()).willReturn(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("owner")
				.build()
		);

		given(userNotificationRepository.saveAll(
				argThat(allOf(
						instanceOf(List.class),
						hasItems(
								allOf(
										isA(UserNotification.class),
										hasProperty("community", equalTo(
												Community.builder()
														.id("123")
														.title("Java User Group")
														.type(CommunityType.OPENED)
														.description("Community for Java developers")
														.build()
										)),
										hasProperty("initiator", equalTo(
												User.builder()
														.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
														.userName("owner")
														.build()
										)),
										hasProperty("recipient", equalTo(
												User.builder()
														.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
														.userName("Toni")
														.build()
										)),
										hasProperty("status", is(UserNotificationStatus.PENDING)),
										hasProperty("type", is(UserNotificationType.INVITATION)),
										hasProperty("creationDatetime", isA(LocalDateTime.class))
								),
								allOf(
										isA(UserNotification.class),
										hasProperty("community", equalTo(
												Community.builder()
														.id("123")
														.title("Java User Group")
														.type(CommunityType.OPENED)
														.description("Community for Java developers")
														.build()
										)),
										hasProperty("initiator", equalTo(
												User.builder()
														.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
														.userName("owner")
														.build()
										)),
										hasProperty("recipient", equalTo(
												User.builder()
														.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
														.userName("Tina")
														.build()
										)),
										hasProperty("status", is(UserNotificationStatus.PENDING)),
										hasProperty("type", is(UserNotificationType.INVITATION)),
										hasProperty("creationDatetime", isA(LocalDateTime.class))
								)
						)
				))
		)).willReturn(Arrays.asList(
				UserNotification.builder()
						.id("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc")
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPENED)
								.description("Community for Java developers")
								.build())
						.initiator(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("owner")
								.build())
						.recipient(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("Toni")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.status(UserNotificationStatus.PENDING)
						.type(UserNotificationType.INVITATION)
						.build(),
				UserNotification.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPENED)
								.description("Community for Java developers")
								.build())
						.initiator(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("owner")
								.build())
						.recipient(		User.builder()
								.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
								.userName("Tina")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.status(UserNotificationStatus.PENDING)
						.type(UserNotificationType.INVITATION)
						.build()
		));

		List<UserNotification> userNotifications = userNotificationCommandService.inviteUsers(Arrays.asList(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("Toni")
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("Tina")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("Community for Java developers")
				.build());

		assertThat(userNotifications).hasSize(2);
		UserNotification userNotification = userNotifications.get(0);
		assertThat(userNotification.getId()).isEqualTo("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc");
		assertThat(userNotification.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("Community for Java developers")
				.build());
		assertThat(userNotification.getInitiator()).isEqualTo(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("owner")
				.build());

		assertThat(userNotification.getRecipient()).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("Toni")
				.build());
		assertThat(userNotification.getStatus()).isEqualTo(UserNotificationStatus.PENDING);
		assertThat(userNotification.getType()).isEqualTo(UserNotificationType.INVITATION);
		assertThat(userNotification.getCreationDatetime()).isOfAnyClassIn(LocalDateTime.class);

		userNotification = userNotifications.get(1);
		assertThat(userNotification.getId()).isEqualTo("56ef4778-a084-4509-9a3e-80b7895cf7b0");
		assertThat(userNotification.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("Community for Java developers")
				.build());
		assertThat(userNotification.getInitiator()).isEqualTo(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("owner")
				.build());

		assertThat(userNotification.getRecipient()).isEqualTo(User.builder()
				.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
				.userName("Tina")
				.build());
		assertThat(userNotification.getStatus()).isEqualTo(UserNotificationStatus.PENDING);
		assertThat(userNotification.getType()).isEqualTo(UserNotificationType.INVITATION);
		assertThat(userNotification.getCreationDatetime()).isOfAnyClassIn(LocalDateTime.class);

	}

	@DisplayName("Test if exception is thrown when there are no users to invite.")
	@Test
	void testIfExceptionIsThrownWhenNoUsesToInvite() {
		assertThrows(IllegalArgumentException.class, () -> userNotificationCommandService.inviteUsers(Collections.emptyList(), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("Community for Java developers")
				.build()));
	}

}
