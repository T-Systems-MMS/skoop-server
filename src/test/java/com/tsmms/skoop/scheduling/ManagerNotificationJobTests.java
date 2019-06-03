package com.tsmms.skoop.scheduling;

import com.tsmms.skoop.email.ManagerNotificationService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.query.UserProjectQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ManagerNotificationJobTests {

	@Mock
	private UserProjectQueryService userProjectQueryService;

	@Mock
	private ManagerNotificationService managerNotificationService;

	private ManagerNotificationJob managerNotificationJob;

	@BeforeEach
	void setUp() {
		this.managerNotificationJob = new ManagerNotificationJob(managerNotificationService,
				userProjectQueryService);
	}

	@DisplayName("Sends notifications.")
	@Test
	void sendNotifications() {
		given(userProjectQueryService.getNotApprovedUserProjects()).willReturn(
				Stream.of(
						UserProject.builder()
								.id("123")
								.user(User.builder()
										.id("aaa")
										.userName("firstUser")
										.firstName("First user")
										.lastName("First user")
										.manager(User.builder()
												.id("abc")
												.userName("manager")
												.firstName("Manager")
												.lastName("Manager")
												.build())
										.build())
								.approved(false)
								.build(),
						UserProject.builder()
								.id("456")
								.user(User.builder()
										.id("bbb")
										.userName("secondUser")
										.firstName("Second user")
										.lastName("Second user")
										.manager(User.builder()
												.id("abc")
												.userName("manager")
												.firstName("Manager")
												.lastName("Manager")
												.build())
										.build())
								.approved(false)
								.build()
				)
		);
		assertDoesNotThrow(() -> managerNotificationJob.sendNotifications());
	}

	@DisplayName("Run sending of notifications when users do not have managers.")
	@Test
	void runSendingOfNotificationsWhenUsersDoNotHaveManagers() {
		given(userProjectQueryService.getNotApprovedUserProjects()).willReturn(
				Stream.of(
						UserProject.builder()
								.id("123")
								.user(User.builder()
										.id("aaa")
										.userName("firstUser")
										.firstName("First user")
										.lastName("First user")
										.build())
								.approved(false)
								.build(),
						UserProject.builder()
								.id("456")
								.user(User.builder()
										.id("bbb")
										.userName("secondUser")
										.firstName("Second user")
										.lastName("Second user")
										.build())
								.approved(false)
								.build()
				)
		);
		assertDoesNotThrow(() -> managerNotificationJob.sendNotifications());
	}

}
