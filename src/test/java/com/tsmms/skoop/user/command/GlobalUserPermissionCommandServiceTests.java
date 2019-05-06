package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.tsmms.skoop.user.command.ReplaceGlobalUserPermissionListCommand.GlobalUserPermissionEntry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tsmms.skoop.user.GlobalUserPermissionScope.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GlobalUserPermissionCommandServiceTests {

	@Mock
	private GlobalUserPermissionRepository globalUserPermissionRepository;

	@Mock
	private UserQueryService userQueryService;

	private GlobalUserPermissionCommandService globalUserPermissionCommandService;

	@BeforeEach
	void setUp() {
		globalUserPermissionCommandService = new GlobalUserPermissionCommandService(globalUserPermissionRepository, userQueryService);
	}

	@DisplayName("Replaces user global permissions.")
	@Test
	void replaceGlobalUserPermissions() {

		given(userQueryService.getUserById("123")).willReturn(
				Optional.of(
						User.builder()
								.id("123")
								.userName("tester")
								.build()
				)
		);

		given(userQueryService.exists("123")).willReturn(true);

		given(globalUserPermissionRepository.saveAll(argThat(allOf(
				isA(Iterable.class),
				containsInAnyOrder(
						allOf(
								isA(GlobalUserPermission.class),
								hasProperty("id", isA(String.class)),
								hasProperty("scope", is(READ_USER_PROFILE)),
								hasProperty("owner", equalTo(
										User.builder()
												.id("123")
												.userName("tester")
												.build()
								))
						),
						allOf(
								isA(GlobalUserPermission.class),
								hasProperty("id", isA(String.class)),
								hasProperty("scope", is(FIND_AS_COACH)),
								hasProperty("owner", equalTo(
										User.builder()
												.id("123")
												.userName("tester")
												.build()
								))
						)
				)
		)))).willReturn(
				Arrays.asList(
						GlobalUserPermission.builder()
								.id("abc")
								.owner(
										User.builder()
												.id("123")
												.userName("tester")
												.build()
								)
								.scope(FIND_AS_COACH)
								.build(),
						GlobalUserPermission.builder()
								.id("def")
								.owner(
										User.builder()
												.id("123")
												.userName("tester")
												.build()
								)
								.scope(READ_USER_PROFILE)
								.build()
				)
		);

		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionCommandService.replaceGlobalUserPermissions(ReplaceGlobalUserPermissionListCommand.builder()
				.ownerId("123")
				.globalPermissions(new HashSet<>(Arrays.asList(GlobalUserPermissionEntry
								.builder()
								.scope(READ_USER_PROFILE)
								.build(),
						GlobalUserPermissionEntry
								.builder()
								.scope(FIND_AS_COACH)
								.build()
				)))
				.build()
		);
		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.owner(
								User.builder()
										.id("123")
										.userName("tester")
										.build()
						)
						.scope(FIND_AS_COACH)
						.build(),
				GlobalUserPermission.builder()
						.id("def")
						.owner(
								User.builder()
										.id("123")
										.userName("tester")
										.build()
						)
						.scope(READ_USER_PROFILE)
						.build()
		);
	}

	@DisplayName("Throws exception if command is null when replacing global user permissions.")
	@Test
	void throwExceptionIfCommandIsNullWhenReplacingGlobalUserPermissions() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionCommandService.replaceGlobalUserPermissions(null));
	}

	@DisplayName("Throws exception if user does not exist when replacing global user permissions.")
	@Test
	void throwExceptionIfUserDoesNotExistWhenReplacingGlobalUserPermissions() {
		given(userQueryService.exists("123")).willReturn(false);
		assertThrows(NoSuchResourceException.class, () -> globalUserPermissionCommandService.replaceGlobalUserPermissions(ReplaceGlobalUserPermissionListCommand.builder()
				.ownerId("123")
				.globalPermissions(new HashSet<>(Arrays.asList(GlobalUserPermissionEntry
								.builder()
								.scope(READ_USER_PROFILE)
								.build(),
						GlobalUserPermissionEntry
								.builder()
								.scope(FIND_AS_COACH)
								.build()
				)))
				.build()
		));
	}

}
