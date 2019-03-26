package io.knowledgeassets.myskills.server.notification;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserRepository;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationRepository;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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
				.id("123456")
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

		Notification communityManagerInvitedToAnotherCommunity = notificationRepository.save(Notification.builder()
				.id("abc")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 10, 0))
				.type(NotificationType.INVITATION_TO_JOIN_COMMUNITY)
				.userRecipient(communityManager)
				.registration(firstRegistration)
				.build());

		// notification that commonUser sent request to join "JavaScript User Group". It will be shown to communityManager.

		Notification requestToJoinJavaScriptUserGroup = notificationRepository.save(Notification.builder()
				.id("def")
				.creationDatetime(LocalDateTime.of(2019, 3, 26, 11, 0))
				.type(NotificationType.REQUEST_TO_JOIN_COMMUNITY)
				.communityRecipient(javascriptUserGroup)
				.registration(secondRegistration)
				.build()
		);

		// notification that commonUser sent request to join "Frontend developers". It will not be shown to communityManager.

		notificationRepository.save(Notification.builder()
				.id("ghi")
				.creationDatetime(LocalDateTime.of(2019, 3, 27, 11, 0))
				.type(NotificationType.REQUEST_TO_JOIN_COMMUNITY)
				.communityRecipient(frontendDevelopers)
				.registration(thirdRegistration)
				.build()
		);

		final Stream<Notification> notifications = notificationRepository.getUserNotifications("123");

		assertThat(notifications).containsExactly(
				requestToJoinJavaScriptUserGroup,
				communityManagerInvitedToAnotherCommunity
		);
	}

}
