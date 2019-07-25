package com.tsmms.skoop.user;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.user.command.ReplaceUserPermissionListCommand;
import com.tsmms.skoop.user.command.UserCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private NotificationCommandService notificationCommandService;

	private UserCommandService userCommandService;

	@BeforeEach
	void setUp() {
		userCommandService = new UserCommandService(userRepository, notificationCommandService);
	}

	@Test
	@DisplayName("Create User")
	void createUser() {
		given(userRepository.findByUserName("tester1")).willReturn(Optional.empty());
		given(userRepository.save(ArgumentMatchers.isA(User.class)))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build());

		User user = userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com");

		assertThat(user).isNotNull();
		assertThat(user.getId()).isNotNull();
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester1");
		assertThat(user.getEmail()).isEqualTo("tester1@gmail.com");
	}

	@Test
	@DisplayName("Create User that has already exist")
	void createUser_ThrowsException() {
		given(userRepository.findByUserName("tester1")).willReturn(Optional.of(
				User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build()));

		assertThrows(DuplicateResourceException.class, () -> {
			userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com");
		});
	}

	@Test
	@DisplayName("Update User")
	void updateUser() {
		given(userRepository.findById("123"))
				.willReturn(Optional.of(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build()));
		given(userRepository.save(ArgumentMatchers.any(User.class)))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build());

		User user = userCommandService.updateUser("123", UserRequest.builder().userName("tester1")
				.firstName("firstTester").lastName(null).email("tester1@gmail.com").coach(true).build());

		assertThat(user).isNotNull();
		assertThat(user.getId()).isNotNull();
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester1");
		assertThat(user.getEmail()).isEqualTo("tester1@gmail.com");
	}

	@DisplayName("Throws exception when non existent user is updated.")
	@Test
	void throwExceptionWhenNonExistentUserIsUpdated() {
		given(userRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userCommandService.updateUser("123", UserRequest.builder()
						.userName("tester")
						.firstName("firstTester")
						.email("tester@skoop.io")
						.build()
		));
	}

	@DisplayName("Updates manager of a user.")
	@Test
	void updateUserManager() {
		given(userRepository.findById("123"))
				.willReturn(Optional.of(User.builder()
						.id("123")
						.userName("tester")
						.firstName("tester")
						.email("tester@skoop.com")
						.build())
				);

		given(userRepository.findByUserName("manager"))
				.willReturn(Optional.of(User.builder()
						.id("456")
						.userName("manager")
						.firstName("manager")
						.email("manager@skoop.com")
						.build())
				);

		given(userRepository.save(
				User.builder()
						.id("123")
						.userName("tester")
						.firstName("tester")
						.email("tester@skoop.com")
						.manager(
								User.builder()
										.id("456")
										.userName("manager")
										.firstName("manager")
										.email("manager@skoop.com")
										.build()
						)
						.build()
		)).willReturn(
				User.builder()
						.id("123")
						.userName("tester")
						.firstName("tester")
						.email("tester@skoop.com")
						.manager(
								User.builder()
										.id("456")
										.userName("manager")
										.firstName("manager")
										.email("manager@skoop.com")
										.build()
						)
						.build()
		);

		final User user = userCommandService.updateUserManager("123", "manager");
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester");
		assertThat(user.getFirstName()).isEqualTo("tester");
		assertThat(user.getEmail()).isEqualTo("tester@skoop.com");
		assertThat(user.getManager()).isEqualTo(
				User.builder()
						.id("456")
						.userName("manager")
						.firstName("manager")
						.email("manager@skoop.com")
						.build()
		);
	}

	@DisplayName("Throws exception when updating manager of non existent user.")
	@Test
	void throwExceptionWhenUserWithSpecifiedIdentifierDoesNotExist() {
		given(userRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userCommandService.updateUserManager("123", "manager"));
	}

	@DisplayName("Throws exception when assigning non existent manager to a user.")
	@Test
	void throwExceptionWhenUserWithSpecifiedManagerUserNameDoesNotExist() {
		given(userRepository.findById("123"))
				.willReturn(Optional.of(User.builder()
						.id("123")
						.userName("tester")
						.firstName("tester")
						.email("tester@skoop.com")
						.build())
				);
		given(userRepository.findByUserName("manager")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userCommandService.updateUserManager("123", "manager"));
	}

	@DisplayName("Deletes user.")
	@Test
	void deleteUser() {
		given(userRepository.findById("123"))
				.willReturn(Optional.of(User.builder()
						.id("123")
						.userName("tester")
						.firstName("tester")
						.email("tester@skoop.com")
						.build())
				);
		assertDoesNotThrow(() -> userCommandService.deleteUser("123"));
	}

	@DisplayName("Throws exception when deleting non existent user.")
	@Test
	void throwExceptionWhenDeletingNonExistentUser() {
		given(userRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> userCommandService.deleteUser("123"));
	}

	@DisplayName("Replaces outbound user permissions.")
	@Test
	void replaceOutboundUserPermissions() {
		given(userRepository.findById("123")).willReturn(Optional.of(
				User.builder()
						.id("123")
						.userName("owner")
						.build()
		));
		given(userRepository.findAllById(new HashSet<>(Arrays.asList("456", "789")))).willReturn(Arrays.asList(
				User.builder()
						.id("456")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("789")
						.userName("secondUser")
						.build()
		));
		given(userRepository.save(
				User.builder()
						.id("123")
						.userName("owner")
						.userPermissions(
								Collections.singletonList(
										UserPermission.builder()
												.id("abc")
												.owner(User.builder()
														.id("123")
														.userName("owner")
														.build())
												.scope(UserPermissionScope.READ_USER_PROFILE)
												.authorizedUsers(Arrays.asList(
														User.builder()
																.id("456")
																.userName("firstUser")
																.build(),
														User.builder()
																.id("789")
																.userName("secondUser")
																.build()
												))
												.build()
								)
						)
						.build()
		)).willReturn(
				User.builder()
						.id("123")
						.userName("owner")
						.userPermissions(
								Collections.singletonList(
										UserPermission.builder()
												.id("abc")
												.owner(User.builder()
														.id("123")
														.userName("owner")
														.build())
												.scope(UserPermissionScope.READ_USER_PROFILE)
												.authorizedUsers(Arrays.asList(
														User.builder()
																.id("456")
																.userName("firstUser")
																.build(),
														User.builder()
																.id("789")
																.userName("secondUser")
																.build()
												))
												.build()
								)
						)
						.build()
		);
		final List<UserPermission> userPermissions = userCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Arrays.asList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
								.scope(UserPermissionScope.READ_USER_PROFILE)
								.authorizedUserIds(Arrays.asList("456", "789"))
								.build(),
						ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
								.scope(UserPermissionScope.READ_USER_SKILLS)
								.build()
				))
				.build()
		).collect(Collectors.toList());
		assertThat(userPermissions).hasSize(1);
		UserPermission userPermission = userPermissions.get(0);
		assertThat(userPermission.getId()).isEqualTo("abc");
		assertThat(userPermission.getOwner()).isEqualTo(User.builder()
				.id("123")
				.userName("owner")
				.build());
		assertThat(userPermission.getAuthorizedUsers()).containsExactlyInAnyOrder(User.builder()
						.id("456")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("789")
						.userName("secondUser")
						.build());
		assertThat(userPermission.getScope()).isEqualTo(UserPermissionScope.READ_USER_PROFILE);
	}

	@DisplayName("The permissions must not be granted to the owner.")
	@Test
	void permissionsMustNotBeGrantedToOwner() {
		assertThrows(IllegalArgumentException.class, () -> userCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Collections.singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.authorizedUserIds(Arrays.asList("123", "456"))
						.build()
				))
				.build()
		));
	}

	@DisplayName("The user is not found when replacing outbound permissions.")
	@Test
	void userNotFoundWhenReplacingOutboundPermissions() {
		given(userRepository.findById("123")).willReturn(Optional.empty());
		given(userRepository.findAllById(new HashSet<>(Arrays.asList("456", "789")))).willReturn(Arrays.asList(
				User.builder()
						.id("456")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("789")
						.userName("secondUser")
						.build()
		));
		assertThrows(NoSuchResourceException.class, () -> userCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Collections.singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.authorizedUserIds(Arrays.asList("456", "789"))
						.build()
				))
				.build()
		));
	}

}
