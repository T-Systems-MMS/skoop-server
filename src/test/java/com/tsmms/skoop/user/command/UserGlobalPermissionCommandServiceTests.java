package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.GlobalPermission;
import com.tsmms.skoop.user.GlobalPermissionRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.tsmms.skoop.user.command.ReplaceUserGlobalPermissionListCommand.UserGlobalPermissionEntry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static com.tsmms.skoop.user.UserPermissionScope.*;
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
class UserGlobalPermissionCommandServiceTests {

	@Mock
	private GlobalPermissionRepository globalPermissionRepository;

	@Mock
	private UserQueryService userQueryService;

	private UserGlobalPermissionCommandService userGlobalPermissionCommandService;

	@BeforeEach
	void setUp() {
		userGlobalPermissionCommandService = new UserGlobalPermissionCommandService(globalPermissionRepository, userQueryService);
	}

	@DisplayName("Replaces user global permissions.")
	@Test
	void replaceUserGlobalPermissions() {

		given(userQueryService.getUserById("123")).willReturn(
				Optional.of(
						User.builder()
						.id("123")
						.userName("tester")
						.build()
				)
		);

		given(globalPermissionRepository.saveAll(argThat(allOf(
				isA(Iterable.class),
				containsInAnyOrder(
						allOf(
								isA(GlobalPermission.class),
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
								isA(GlobalPermission.class),
								hasProperty("id", isA(String.class)),
								hasProperty("scope", is(SEE_AS_COACH)),
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
						GlobalPermission.builder()
								.id("abc")
								.owner(
										User.builder()
												.id("123")
												.userName("tester")
												.build()
								)
								.scope(SEE_AS_COACH)
								.build(),
						GlobalPermission.builder()
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

		final Stream<GlobalPermission> globalPermissions = userGlobalPermissionCommandService.replaceUserGlobalPermissions(ReplaceUserGlobalPermissionListCommand.builder()
				.ownerId("123")
				.globalPermissions(new HashSet<>(Arrays.asList(UserGlobalPermissionEntry
								.builder()
								.scope(READ_USER_PROFILE)
								.build(),
						UserGlobalPermissionEntry
								.builder()
								.scope(SEE_AS_COACH)
								.build()
				)))
				.build()
		);
		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalPermission.builder()
						.id("abc")
						.owner(
								User.builder()
										.id("123")
										.userName("tester")
										.build()
						)
						.scope(SEE_AS_COACH)
						.build(),
				GlobalPermission.builder()
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

	@DisplayName("Throws exception if command is null when replacing user global permissions.")
	@Test
	void throwExceptionIfCommandIsNullWhenReplacingUserGlobalPermissions() {
		assertThrows(IllegalArgumentException.class, () -> userGlobalPermissionCommandService.replaceUserGlobalPermissions(null));
	}

}
