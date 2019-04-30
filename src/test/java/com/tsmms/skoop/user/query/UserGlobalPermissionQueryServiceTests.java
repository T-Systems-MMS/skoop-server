package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.GlobalPermission;
import com.tsmms.skoop.user.GlobalPermissionRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermissionScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserGlobalPermissionQueryServiceTests {

	@Mock
	private GlobalPermissionRepository globalPermissionRepository;

	private UserGlobalPermissionQueryService userGlobalPermissionQueryService;

	@BeforeEach
	void setUp() {
		userGlobalPermissionQueryService = new UserGlobalPermissionQueryService(globalPermissionRepository);
	}

	@DisplayName("Gets user global permissions.")
	@Test
	void getUserGlobalPermissions() {
		given(globalPermissionRepository.findByOwnerId("123")).willReturn(
				Stream.of(
						GlobalPermission.builder()
								.id("abc")
								.scope(UserPermissionScope.READ_USER_PROFILE)
								.owner(User.builder()
										.id("123")
										.userName("tester")
										.build()
								)
								.build(),
						GlobalPermission.builder()
								.id("abc")
								.scope(UserPermissionScope.SEE_AS_COACH)
								.owner(User.builder()
										.id("123")
										.userName("tester")
										.build()
								)
								.build()
				)
		);
		final Stream<GlobalPermission> globalPermissions = userGlobalPermissionQueryService.getUserGlobalPermissions("123");

		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalPermission.builder()
						.id("abc")
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.owner(User.builder()
								.id("123")
								.userName("tester")
								.build()
						)
						.build(),
				GlobalPermission.builder()
						.id("abc")
						.scope(UserPermissionScope.SEE_AS_COACH)
						.owner(User.builder()
								.id("123")
								.userName("tester")
								.build()
						)
						.build()
		);
	}

	@DisplayName("Throws exception if owner ID is null when getting user global permissions.")
	@Test
	void throwExceptionIfOwnerIdIsNullWhenGettingUserGlobalPermissions() {
		assertThrows(IllegalArgumentException.class, () -> userGlobalPermissionQueryService.getUserGlobalPermissions(null));
	}

	@DisplayName("Check if global permissions is granted.")
	@Test
	void isGlobalPermissionGranted() {
		given(globalPermissionRepository.isGlobalPermissionGranted("123", UserPermissionScope.READ_USER_PROFILE)).willReturn(true);
		assertThat(userGlobalPermissionQueryService.isGlobalPermissionGranted("123", UserPermissionScope.READ_USER_PROFILE)).isTrue();
	}

	@DisplayName("Throws exception if owner ID is null when checking if global permission granted.")
	@Test
	void throwExceptionIfOwnerIdIsNullWhenCheckingIfGlobalPermissionGranted() {
		assertThrows(IllegalArgumentException.class, () -> userGlobalPermissionQueryService.isGlobalPermissionGranted(null, UserPermissionScope.READ_USER_PROFILE));
	}

	@DisplayName("Throws exception if scope is null when checking if global permission granted.")
	@Test
	void throwExceptionIfScopeIsNullWhenCheckingIfGlobalPermissionGranted() {
		assertThrows(IllegalArgumentException.class, () -> userGlobalPermissionQueryService.isGlobalPermissionGranted("123", null));
	}

}
