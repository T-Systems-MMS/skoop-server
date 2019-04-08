package com.tsmms.skoop.notification.query;


import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityChangedNotification;
import com.tsmms.skoop.community.CommunityDeletedNotification;
import com.tsmms.skoop.community.CommunityDetails;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.communityuser.CommunityUserRoleChangedNotification;
import com.tsmms.skoop.communityuser.UserKickedOutFromCommunityNotification;
import com.tsmms.skoop.communityuser.UserLeftCommunityNotification;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.InvitationToJoinCommunityNotification;
import com.tsmms.skoop.communityuser.registration.RequestToJoinCommunityNotification;
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
import java.util.Arrays;
import java.util.HashSet;
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
						.communityName("JavaScript User Group")
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
						.communityName("AnotherCommunity")
						.build(),
				CommunityDeletedNotification.builder()
						.id("iop")
						.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
						.communityName("Deleted community")
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.build(),
				CommunityUserRoleChangedNotification.builder()
						.id("yyy")
						.role(CommunityRole.MANAGER)
						.communityName("Community the role has been changed in")
						.creationDatetime(LocalDateTime.of(2018, 4, 3, 12, 0))
						.user(User.builder()
								.id("abc")
								.userName("tester")
								.build())
						.build(),
				CommunityChangedNotification.builder()
						.id("ttt")
						.communityName("Changed community")
						.creationDatetime(LocalDateTime.of(2017, 1, 3, 10, 0))
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.communityDetails(new HashSet<>(Arrays.asList(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE)))
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
						.communityName("JavaScript User Group")
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
						.communityName("AnotherCommunity")
						.build(),
				CommunityDeletedNotification.builder()
						.id("iop")
						.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
						.communityName("Deleted community")
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.build(),
				CommunityUserRoleChangedNotification.builder()
						.id("yyy")
						.role(CommunityRole.MANAGER)
						.communityName("Community the role has been changed in")
						.creationDatetime(LocalDateTime.of(2018, 4, 3, 12, 0))
						.user(User.builder()
								.id("abc")
								.userName("tester")
								.build())
						.build(),
				CommunityChangedNotification.builder()
						.id("ttt")
						.communityName("Changed community")
						.creationDatetime(LocalDateTime.of(2017, 1, 3, 10, 0))
						.recipients(singletonList(User.builder()
								.id("abc")
								.userName("tester")
								.build()))
						.communityDetails(new HashSet<>(Arrays.asList(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE)))
						.build()
		);
	}

}
