package com.tsmms.skoop.notification;

import com.tsmms.skoop.community.CommunityChangedNotification;
import com.tsmms.skoop.community.CommunityDeletedNotification;
import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityDetails;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.CommunityUserRepository;
import com.tsmms.skoop.communityuser.CommunityUserRoleChangedNotification;
import com.tsmms.skoop.communityuser.UserKickedOutFromCommunityNotification;
import com.tsmms.skoop.communityuser.UserLeftCommunityNotification;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import com.tsmms.skoop.communityuser.registration.InvitationToJoinCommunityNotification;
import com.tsmms.skoop.communityuser.registration.RequestToJoinCommunityNotification;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.toList;
import static java.util.Collections.singletonList;

@DataNeo4jTest
class NotificationRepositoryTests {

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommunityUserRegistrationRepository communityUserRegistrationRepository;

	@Autowired
	private CommunityUserRepository communityUserRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@DisplayName("Get notifications sent to the user and to the communities she / he is the manager of.")
	@Test
	void getsUserNotifications() {

		User communityManager = userRepository.save(User.builder()
				.id("123")
				.userName("communityManager")
				.build());

		User commonUser = userRepository.save(User.builder()
				.id("456")
				.userName("commonUser")
				.build());

		Community javascriptUserGroup = communityRepository.save(Community.builder()
				.id("567")
				.title("JavaScript User Group")
				.type(CommunityType.CLOSED)
				.build()
		);

		Community frontendDevelopers = communityRepository.save(Community.builder()
				.id("567123")
				.title("Frontend developers")
				.type(CommunityType.CLOSED)
				.build()
		);

		// communityManager is a community manager of "JavaScript User Group"

		communityUserRepository.save(CommunityUser.builder()
				.creationDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.community(javascriptUserGroup)
				.user(communityManager)
				.role(CommunityRole.MANAGER)
				.build()
		);

		// communityManager is a member of "JavaScript User Group"

		communityUserRepository.save(CommunityUser.builder()
				.creationDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.community(frontendDevelopers)
				.user(communityManager)
				.role(CommunityRole.MEMBER)
				.build()
		);

		// communityManager was invited to join "Java User Group"

		CommunityUserRegistration firstRegistration = communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.id("654321")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 10, 0))
				.approvedByCommunity(true)
				.approvedByUser(null)
				.registeredUser(communityManager)
				.community(Community.builder()
						.id("987")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
				)
				.build()
		);

		// commonUser sent request to join "JavaScript User Group"

		CommunityUserRegistration secondRegistration = communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.id("123456")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 11, 0))
				.approvedByCommunity(null)
				.approvedByUser(true)
				.registeredUser(commonUser)
				.community(javascriptUserGroup)
				.build()
		);

		// commonUser sent request to join "Frontend developers"

		CommunityUserRegistration thirdRegistration = communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.id("123456789")
				.creationDatetime(LocalDateTime.of(2019, 3, 27, 11, 0))
				.approvedByCommunity(null)
				.approvedByUser(true)
				.registeredUser(commonUser)
				.community(frontendDevelopers)
				.build()
		);

		// notification that communityManager was invited to join "Java User Group". It will be shown to communityManager.

		notificationRepository.save(InvitationToJoinCommunityNotification.builder()
				.id("abc")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 10, 0))
				.registration(firstRegistration)
				.communityName("Java User Group")
				.build());

		// notification that commonUser sent request to join "JavaScript User Group". It will be shown to communityManager.

		notificationRepository.save(RequestToJoinCommunityNotification.builder()
				.id("def")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 11, 0))
				.registration(secondRegistration)
				.communityName("JavaScript User Group")
				.build()
		);

		// notification that commonUser sent request to join "Frontend developers". It will not be shown to communityManager.

		notificationRepository.save(RequestToJoinCommunityNotification.builder()
				.id("ghi")
				.creationDatetime(LocalDateTime.of(2019, 3, 27, 11, 0))
				.registration(thirdRegistration)
				.communityName("Frontend developers")
				.build()
		);

		notificationRepository.save(CommunityUserRoleChangedNotification.builder()
				.id("yyy")
				.role(CommunityRole.MANAGER)
				.communityName("Community the role has been changed in.")
				.creationDatetime(LocalDateTime.of(2018, 4, 3, 12, 0))
				.user(communityManager)
				.build()
		);

		notificationRepository.save(UserLeftCommunityNotification.builder()
				.id("zyx")
				.creationDatetime(LocalDateTime.of(2019, 3, 21, 15, 30))
				.community(javascriptUserGroup)
				.user(User.builder()
						.id("0123")
						.userName("UserLeftCommunity")
						.build())
				.communityName("JavaScript User Group")
				.build()
		);

		notificationRepository.save(CommunityDeletedNotification.builder()
				.id("iop")
				.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
				.communityName("Deleted community")
				.recipients(singletonList(communityManager))
				.build()
		);

		notificationRepository.save(CommunityChangedNotification.builder()
				.id("ttt")
				.communityName("Changed community")
				.creationDatetime(LocalDateTime.of(2017, 1, 3, 10, 0))
				.recipients(singletonList(communityManager))
				.communityDetails(new HashSet<>(Arrays.asList(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE)))
				.community(Community.builder()
						.id("123456789")
						.title("Changed community")
						.build()
				)
				.build()
		);

		final List<Notification> notifications = notificationRepository.getUserNotifications("123").collect(toList());

		Assertions.assertThat(notifications).hasSize(6);
		Notification notification = notifications.get(0);
		assertThat(notification.getId()).isEqualTo("def");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 11, 0));
		assertThat(notification).isInstanceOf(RequestToJoinCommunityNotification.class);
		RequestToJoinCommunityNotification requestToJoinCommunityNotification = (RequestToJoinCommunityNotification) notification;
		assertThat(requestToJoinCommunityNotification.getRegistration()).isNotNull();
		assertThat(requestToJoinCommunityNotification.getRegistration().getApprovedByUser()).isTrue();
		assertThat(requestToJoinCommunityNotification.getRegistration().getApprovedByCommunity()).isNull();
		assertThat(requestToJoinCommunityNotification.getRegistration().getCommunity()).isEqualTo(javascriptUserGroup);
		assertThat(requestToJoinCommunityNotification.getRegistration().getRegisteredUser()).isEqualTo(commonUser);
		assertThat(requestToJoinCommunityNotification.getRegistration().getId()).isEqualTo("123456");
		assertThat(requestToJoinCommunityNotification.getRegistration().getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 11, 0));
		assertThat(requestToJoinCommunityNotification.getCommunityName()).isEqualTo("JavaScript User Group");

		notification = notifications.get(1);
		assertThat(notification.getId()).isEqualTo("abc");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 10, 0));
		assertThat(notification).isInstanceOf(InvitationToJoinCommunityNotification.class);
		InvitationToJoinCommunityNotification invitationToJoinCommunityNotification = (InvitationToJoinCommunityNotification) notification;
		assertThat(invitationToJoinCommunityNotification.getRegistration()).isNotNull();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByUser()).isNull();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByCommunity()).isTrue();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCommunity()).isEqualTo(Community.builder()
				.id("987")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build());
		assertThat(invitationToJoinCommunityNotification.getRegistration().getRegisteredUser()).isEqualTo(communityManager);
		assertThat(invitationToJoinCommunityNotification.getRegistration().getId()).isEqualTo("654321");
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 10, 0));
		assertThat(invitationToJoinCommunityNotification.getCommunityName()).isEqualTo("Java User Group");

		notification = notifications.get(2);
		assertThat(notification.getId()).isEqualTo("zyx");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 21, 15, 30));
		assertThat(notification).isInstanceOf(UserLeftCommunityNotification.class);
		final UserLeftCommunityNotification userLeftCommunityNotification = (UserLeftCommunityNotification) notification;
		assertThat(userLeftCommunityNotification.getCommunity()).isEqualTo(javascriptUserGroup);
		assertThat(userLeftCommunityNotification.getUser()).isEqualTo(User.builder()
				.id("0123")
				.userName("UserLeftCommunity")
				.build());
		assertThat(userLeftCommunityNotification.getCommunityName()).isEqualTo("JavaScript User Group");

		notification = notifications.get(3);
		assertThat(notification.getId()).isEqualTo("iop");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 3, 10, 0));
		assertThat(notification).isInstanceOf(CommunityDeletedNotification.class);
		final CommunityDeletedNotification communityDeletedNotification = (CommunityDeletedNotification) notification;
		assertThat(communityDeletedNotification.getCommunityName()).isEqualTo("Deleted community");
		assertThat(communityDeletedNotification.getRecipients()).contains(communityManager);

		notification = notifications.get(4);
		assertThat(notification.getId()).isEqualTo("yyy");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2018, 4, 3, 12, 0));
		assertThat(notification).isInstanceOf(CommunityUserRoleChangedNotification.class);
		final CommunityUserRoleChangedNotification communityUserRoleChangedNotification = (CommunityUserRoleChangedNotification) notification;
		assertThat(communityUserRoleChangedNotification.getRole()).isEqualTo(CommunityRole.MANAGER);
		assertThat(communityUserRoleChangedNotification.getCommunityName()).isEqualTo("Community the role has been changed in.");
		assertThat(communityUserRoleChangedNotification.getUser()).isEqualTo(communityManager);

		notification = notifications.get(5);
		assertThat(notification.getId()).isEqualTo("ttt");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2017, 1, 3, 10, 0));
		assertThat(notification).isInstanceOf(CommunityChangedNotification.class);
		final CommunityChangedNotification communityChangedNotification = (CommunityChangedNotification) notification;
		assertThat(communityChangedNotification.getRecipients()).isEqualTo(singletonList(communityManager));
		assertThat(communityChangedNotification.getCommunityDetails()).containsExactlyInAnyOrder(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE);
		assertThat(communityChangedNotification.getCommunityName()).isEqualTo("Changed community");
		assertThat(communityChangedNotification.getCommunity()).isEqualTo(Community.builder()
				.id("123456789")
				.title("Changed community")
				.build());
	}

	@DisplayName("Get notifications sent to the user.")
	@Test
	void getsNotificationsSentToUser() {

		User commonUser = userRepository.save(User.builder()
				.id("123")
				.userName("commonUser")
				.build());

		// commonUser was invited to join "Java User Group"

		CommunityUserRegistration firstRegistration = communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.id("654321")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 10, 0))
				.approvedByCommunity(true)
				.approvedByUser(null)
				.registeredUser(commonUser)
				.community(Community.builder()
						.id("987")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
				)
				.build()
		);

		// notification that commonUser was invited to join "Java User Group". It will be shown to commonUser.

		notificationRepository.save(InvitationToJoinCommunityNotification.builder()
				.id("abc")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 10, 0))
				.registration(firstRegistration)
				.communityName("Java User Group")
				.build());

		notificationRepository.save(UserKickedOutFromCommunityNotification.builder()
				.id("def")
				.creationDatetime(LocalDateTime.of(2019, 3, 25, 10, 0))
				.user(commonUser)
				.community(Community.builder()
						.id("145")
						.title("JavaScript User Group")
						.type(CommunityType.CLOSED)
						.build()
				)
				.communityName("JavaScript User Group")
				.build()
		);

		notificationRepository.save(CommunityDeletedNotification.builder()
				.id("iop")
				.creationDatetime(LocalDateTime.of(2019, 1, 3, 10, 0))
				.communityName("Deleted community")
				.recipients(singletonList(commonUser))
				.build()
		);

		notificationRepository.save(CommunityUserRoleChangedNotification.builder()
				.id("yyy")
				.role(CommunityRole.MANAGER)
				.communityName("Community the role has been changed in")
				.creationDatetime(LocalDateTime.of(2018, 4, 3, 12, 0))
				.user(commonUser)
				.build()
		);

		notificationRepository.save(CommunityChangedNotification.builder()
				.id("ttt")
				.communityName("Changed community")
				.creationDatetime(LocalDateTime.of(2017, 1, 3, 10, 0))
				.recipients(singletonList(commonUser))
				.community(Community.builder()
						.id("123456789")
						.title("Changed community")
						.build())
				.communityDetails(new HashSet<>(Arrays.asList(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE)))
				.build()
		);

		final List<Notification> notifications = notificationRepository.getUserNotifications("123").collect(toList());

		Assertions.assertThat(notifications).hasSize(5);

		Notification notification = notifications.get(0);
		assertThat(notification.getId()).isEqualTo("abc");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 10, 0));
		assertThat(notification).isInstanceOf(InvitationToJoinCommunityNotification.class);
		InvitationToJoinCommunityNotification invitationToJoinCommunityNotification = (InvitationToJoinCommunityNotification) notification;
		assertThat(invitationToJoinCommunityNotification.getRegistration()).isNotNull();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByUser()).isNull();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getApprovedByCommunity()).isTrue();
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCommunity()).isEqualTo(Community.builder()
				.id("987")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build());
		assertThat(invitationToJoinCommunityNotification.getRegistration().getRegisteredUser()).isEqualTo(commonUser);
		assertThat(invitationToJoinCommunityNotification.getRegistration().getId()).isEqualTo("654321");
		assertThat(invitationToJoinCommunityNotification.getRegistration().getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 10, 0));
		assertThat(invitationToJoinCommunityNotification.getCommunityName()).isEqualTo("Java User Group");

		notification = notifications.get(1);
		assertThat(notification.getId()).isEqualTo("def");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 25, 10, 0));
		assertThat(notification).isInstanceOf(UserKickedOutFromCommunityNotification.class);
		final UserKickedOutFromCommunityNotification userKickedOutFromCommunityNotification = (UserKickedOutFromCommunityNotification) notification;
		assertThat(userKickedOutFromCommunityNotification.getUser()).isEqualTo(commonUser);
		assertThat(userKickedOutFromCommunityNotification.getCommunity()).isEqualTo(Community.builder()
				.id("145")
				.title("JavaScript User Group")
				.type(CommunityType.CLOSED)
				.build());
		assertThat(userKickedOutFromCommunityNotification.getCommunityName()).isEqualTo("JavaScript User Group");

		notification = notifications.get(2);
		assertThat(notification.getId()).isEqualTo("iop");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 3, 10, 0));
		assertThat(notification).isInstanceOf(CommunityDeletedNotification.class);
		final CommunityDeletedNotification communityDeletedNotification = (CommunityDeletedNotification) notification;
		assertThat(communityDeletedNotification.getCommunityName()).isEqualTo("Deleted community");
		assertThat(communityDeletedNotification.getRecipients()).contains(commonUser);

		notification = notifications.get(3);
		assertThat(notification.getId()).isEqualTo("yyy");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2018, 4, 3, 12, 0));
		assertThat(notification).isInstanceOf(CommunityUserRoleChangedNotification.class);
		final CommunityUserRoleChangedNotification communityUserRoleChangedNotification = (CommunityUserRoleChangedNotification) notification;
		assertThat(communityUserRoleChangedNotification.getRole()).isEqualTo(CommunityRole.MANAGER);
		assertThat(communityUserRoleChangedNotification.getCommunityName()).isEqualTo("Community the role has been changed in");
		assertThat(communityUserRoleChangedNotification.getUser()).isEqualTo(commonUser);

		notification = notifications.get(4);
		assertThat(notification.getId()).isEqualTo("ttt");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2017, 1, 3, 10, 0));
		assertThat(notification).isInstanceOf(CommunityChangedNotification.class);
		final CommunityChangedNotification communityChangedNotification = (CommunityChangedNotification) notification;
		assertThat(communityChangedNotification.getRecipients()).isEqualTo(singletonList(commonUser));
		assertThat(communityChangedNotification.getCommunityDetails()).containsExactlyInAnyOrder(CommunityDetails.DESCRIPTION, CommunityDetails.TYPE);
		assertThat(communityChangedNotification.getCommunityName()).isEqualTo("Changed community");
		assertThat(communityChangedNotification.getCommunity()).isEqualTo(Community.builder()
				.id("123456789")
				.title("Changed community")
				.build());
	}

	@DisplayName("Get notifications sent to the communities the user is the manager of.")
	@Test
	void getsNotificationsSentToCommunitiesUserIsManagerOf() {

		User communityManager = userRepository.save(User.builder()
				.id("123")
				.userName("communityManager")
				.build());

		User commonUser = userRepository.save(User.builder()
				.id("456")
				.userName("commonUser")
				.build());

		Community javascriptUserGroup = communityRepository.save(Community.builder()
				.id("567")
				.title("JavaScript User Group")
				.type(CommunityType.CLOSED)
				.build()
		);

		// communityManager is a community manager of "JavaScript User Group"

		communityUserRepository.save(CommunityUser.builder()
				.creationDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 20, 10, 0))
				.community(javascriptUserGroup)
				.user(communityManager)
				.role(CommunityRole.MANAGER)
				.build()
		);

		// commonUser sent request to join "JavaScript User Group"

		CommunityUserRegistration firstRegistration = communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.id("123456")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 11, 0))
				.approvedByCommunity(null)
				.approvedByUser(true)
				.registeredUser(commonUser)
				.community(javascriptUserGroup)
				.build()
		);

		// notification that commonUser sent request to join "JavaScript User Group".

		notificationRepository.save(RequestToJoinCommunityNotification.builder()
				.id("def")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 11, 0))
				.registration(firstRegistration)
				.communityName("JavaScript User Group")
				.build()
		);

		final List<Notification> notifications = notificationRepository.getUserNotifications("123").collect(toList());

		Assertions.assertThat(notifications).hasSize(1);
		Notification notification = notifications.get(0);
		assertThat(notification.getId()).isEqualTo("def");
		assertThat(notification.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 11, 0));
		assertThat(notification).isInstanceOf(RequestToJoinCommunityNotification.class);
		RequestToJoinCommunityNotification requestToJoinCommunityNotification = (RequestToJoinCommunityNotification) notification;
		assertThat(requestToJoinCommunityNotification.getRegistration()).isNotNull();
		assertThat(requestToJoinCommunityNotification.getRegistration().getApprovedByUser()).isTrue();
		assertThat(requestToJoinCommunityNotification.getRegistration().getApprovedByCommunity()).isNull();
		assertThat(requestToJoinCommunityNotification.getRegistration().getCommunity()).isEqualTo(javascriptUserGroup);
		assertThat(requestToJoinCommunityNotification.getRegistration().getRegisteredUser()).isEqualTo(commonUser);
		assertThat(requestToJoinCommunityNotification.getRegistration().getId()).isEqualTo("123456");
		assertThat(requestToJoinCommunityNotification.getRegistration().getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 3, 26, 11, 0));
		assertThat(requestToJoinCommunityNotification.getCommunityName()).isEqualTo("JavaScript User Group");
	}

}
