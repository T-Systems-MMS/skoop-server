package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserPermissionCommandServiceTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserPermissionRepository userPermissionRepository;

	private UserPermissionCommandService userPermissionCommandService;

	@BeforeEach
	void setUp() {
		this.userPermissionCommandService = new UserPermissionCommandService(userRepository, userPermissionRepository);
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
		given(userPermissionRepository.save(argThat(allOf(
				isA(UserPermission.class),
				hasProperty("id", isA(String.class)),
				hasProperty("owner", equalTo(User.builder()
						.id("123")
						.userName("owner")
						.build())),
				hasProperty("authorizedUsers", equalTo(Arrays.asList(
						User.builder()
								.id("456")
								.userName("firstUser")
								.build(),
						User.builder()
								.id("789")
								.userName("secondUser")
								.build()
				)))
				))
		)).willReturn(UserPermission.builder()
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
		);
		final List<UserPermission> userPermissions = userPermissionCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Collections.singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.authorizedUserIds(Arrays.asList("456", "789"))
						.allUsersAuthorized(false)
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

	@DisplayName("Throws exception when all users are authenticated and authenticated user ids is not empty.")
	@Test
	void throwsExceptionWhenAllUsersAreAuthenticatedAndAuthenticatedUserIdsIsNotEmpty() {
		assertThrows(IllegalArgumentException.class, () -> userPermissionCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Collections.singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.authorizedUserIds(Arrays.asList("456", "789"))
						.allUsersAuthorized(true)
						.build()
				))
				.build()
		));
	}

	@DisplayName("Sets permission to all users.")
	@Test
	void setPermissionToAllUsers() {
		given(userRepository.findById("123")).willReturn(Optional.of(
				User.builder()
						.id("123")
						.userName("owner")
						.build()
		));
		given(userRepository.findAll()).willReturn(Arrays.asList(
				User.builder()
						.id("456")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("789")
						.userName("secondUser")
						.build()
		));
		given(userPermissionRepository.save(argThat(allOf(
				isA(UserPermission.class),
				hasProperty("id", isA(String.class)),
				hasProperty("owner", equalTo(User.builder()
						.id("123")
						.userName("owner")
						.build())),
				hasProperty("authorizedUsers", equalTo(Arrays.asList(
						User.builder()
								.id("456")
								.userName("firstUser")
								.build(),
						User.builder()
								.id("789")
								.userName("secondUser")
								.build()
				)))
				))
		)).willReturn(UserPermission.builder()
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
		);
		final List<UserPermission> userPermissions = userPermissionCommandService.replaceOutboundUserPermissions(ReplaceUserPermissionListCommand.builder()
				.ownerId("123")
				.userPermissions(Collections.singletonList(ReplaceUserPermissionListCommand.UserPermissionEntry.builder()
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.allUsersAuthorized(true)
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

}
