package com.tsmms.skoop.email;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userproject.UserProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ManagerNotificationServiceTests {

	@Mock
	private EmailService emailService;

	private ManagerNotificationService managerNotificationService;

	@BeforeEach
	void setUp() throws IOException {
		this.managerNotificationService = new ManagerNotificationService(emailService,
				"There are user project memberships pending for your approval.",
				"http://localhost:4200/my-subordinates/{subordinateId}/project-memberships");
	}

	@DisplayName("Sends notification.")
	@Test
	void sendNotification() {
		assertDoesNotThrow(() -> managerNotificationService.send(User.builder()
						.id("abc")
						.userName("manager")
						.firstName("Manager")
						.lastName("Manager")
						.build(),
				Arrays.asList(
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
		));
	}

	@DisplayName("Throws exception when sending notification to manager if manager is null.")
	@Test
	void throwExceptionWhenSendingNotificationToManagerIfManagerIsNull() {
		assertThrows(IllegalArgumentException.class, () -> managerNotificationService.send(null, Collections.singletonList(
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
						.build()
		)));
	}

	@DisplayName("Throws exception when sending notification to manager if user project membership collection is null.")
	@Test
	void throwExceptionWhenSendingNotificationToManagerIfUserProjectMembershipCollectionIsNull() {
		assertThrows(IllegalArgumentException.class, () -> managerNotificationService.send(User.builder()
				.id("abc")
				.userName("manager")
				.firstName("Manager")
				.lastName("Manager")
				.build(), null
		));
	}

	@DisplayName("Sends notification when user project membership collection is empty.")
	@Test
	void sendsNotificationWhenUserProjectMembershipCollectionIsEmpty() {
		assertDoesNotThrow(() -> managerNotificationService.send(User.builder()
				.id("abc")
				.userName("manager")
				.firstName("Manager")
				.lastName("Manager")
				.build(), Collections.emptyList()
		));
	}

}
