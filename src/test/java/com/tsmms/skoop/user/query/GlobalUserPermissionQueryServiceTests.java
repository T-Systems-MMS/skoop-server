package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionRepository;
import com.tsmms.skoop.user.User;
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
import static com.tsmms.skoop.user.GlobalUserPermissionScope.*;

@ExtendWith(MockitoExtension.class)
class GlobalUserPermissionQueryServiceTests {

	@Mock
	private GlobalUserPermissionRepository globalUserPermissionRepository;

	private GlobalUserPermissionQueryService globalUserPermissionQueryService;

	@BeforeEach
	void setUp() {
		globalUserPermissionQueryService = new GlobalUserPermissionQueryService(globalUserPermissionRepository);
	}

	@DisplayName("Gets user global permissions.")
	@Test
	void getGlobalUserPermissions() {
		given(globalUserPermissionRepository.findByOwnerId("123")).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("abc")
								.scope(READ_USER_PROFILE)
								.owner(User.builder()
										.id("123")
										.userName("tester")
										.build()
								)
								.build(),
						GlobalUserPermission.builder()
								.id("abc")
								.scope(FIND_AS_COACH)
								.owner(User.builder()
										.id("123")
										.userName("tester")
										.build()
								)
								.build()
				)
		);
		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionQueryService.getGlobalUserPermissions("123");

		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.scope(READ_USER_PROFILE)
						.owner(User.builder()
								.id("123")
								.userName("tester")
								.build()
						)
						.build(),
				GlobalUserPermission.builder()
						.id("abc")
						.scope(FIND_AS_COACH)
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
	void throwExceptionIfOwnerIdIsNullWhenGettingGlobalUserPermissions() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getGlobalUserPermissions(null));
	}

	@DisplayName("Check if global permissions is granted.")
	@Test
	void isGlobalUserPermissionGranted() {
		given(globalUserPermissionRepository.isGlobalPermissionGranted("123", READ_USER_PROFILE)).willReturn(true);
		assertThat(globalUserPermissionQueryService.isGlobalUserPermissionGranted("123", READ_USER_PROFILE)).isTrue();
	}

	@DisplayName("Throws exception if owner ID is null when checking if global permission granted.")
	@Test
	void throwExceptionIfOwnerIdIsNullWhenCheckingIfGlobalPermissionGranted() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.isGlobalUserPermissionGranted(null, READ_USER_PROFILE));
	}

	@DisplayName("Throws exception if scope is null when checking if global permission granted.")
	@Test
	void throwExceptionIfScopeIsNullWhenCheckingIfGlobalPermissionGranted() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.isGlobalUserPermissionGranted("123", null));
	}

}
