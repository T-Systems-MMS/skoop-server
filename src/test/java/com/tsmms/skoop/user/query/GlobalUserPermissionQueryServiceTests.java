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

import java.util.Arrays;
import java.util.Collections;
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

	@DisplayName("Gets outbound user global permissions.")
	@Test
	void getOutboundGlobalUserPermissions() {
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
		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionQueryService.getOutboundGlobalUserPermissions("123");

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

	@DisplayName("Throws exception if owner ID is null when getting user outbound global permissions.")
	@Test
	void throwExceptionIfOwnerIdIsNullWhenGettingOutboundGlobalUserPermissions() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getOutboundGlobalUserPermissions(null));
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

	@DisplayName("Gets outbound user global permissions by scope.")
	@Test
	void getOutboundGlobalUserPermissionsByScope() {
		given(globalUserPermissionRepository.findByOwnerIdAndScope("123", READ_USER_PROFILE)).willReturn(
				Stream.of(
						GlobalUserPermission.builder()
								.id("abc")
								.scope(READ_USER_PROFILE)
								.owner(User.builder()
										.id("123")
										.userName("tester")
										.build()
								)
								.build()
				)
		);
		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionQueryService.getOutboundGlobalUserPermissionsByScope("123", READ_USER_PROFILE);

		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.scope(READ_USER_PROFILE)
						.owner(User.builder()
								.id("123")
								.userName("tester")
								.build()
						)
						.build()
		);
	}

	@DisplayName("Throws exception if owner ID is null when getting user outbound global permissions.")
	@Test
	void throwExceptionIfOwnerIdIsNullWhenGettingOutboundGlobalUserPermissionsByScope() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getOutboundGlobalUserPermissionsByScope(null, READ_USER_PROFILE));
	}

	@DisplayName("Throws exception if owner ID is null when getting user outbound global permissions.")
	@Test
	void throwExceptionIfScopeIsNullWhenGettingOutboundGlobalUserPermissionsByScope() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getOutboundGlobalUserPermissionsByScope("123", null));
	}


	@DisplayName("Gets inbound user global permissions by scope.")
	@Test
	void getInboundGlobalUserPermissionsByScope() {
		given(globalUserPermissionRepository.getInboundGlobalUserPermissionsByScope("123", READ_USER_PROFILE)).willReturn(
				Collections.singleton(
						GlobalUserPermission.builder()
								.id("abc")
								.scope(READ_USER_PROFILE)
								.owner(User.builder()
										.id("456")
										.userName("tester")
										.build()
								)
								.build()
				)
		);
		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionQueryService.getInboundGlobalUserPermissionsByScope("123", READ_USER_PROFILE);

		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.scope(READ_USER_PROFILE)
						.owner(User.builder()
								.id("456")
								.userName("tester")
								.build()
						)
						.build()
		);
	}

	@DisplayName("Throws exception if owner ID is null when getting user inbound global permissions.")
	@Test
	void throwExceptionIfUserIdIsNullWhenGettingInboundGlobalUserPermissionsByScope() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getInboundGlobalUserPermissionsByScope(null, READ_USER_PROFILE));
	}

	@DisplayName("Throws exception if owner ID is null when getting user inbound global permissions.")
	@Test
	void throwExceptionIfScopeIsNullWhenGettingInboundGlobalUserPermissionsByScope() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getInboundGlobalUserPermissionsByScope("123", null));
	}

	@DisplayName("Gets inbound user global permissions.")
	@Test
	void getInboundGlobalUserPermissions() {
		given(globalUserPermissionRepository.getInboundGlobalUserPermissions("123")).willReturn(
				Arrays.asList(
						GlobalUserPermission.builder()
								.id("abc")
								.scope(FIND_AS_COACH)
								.owner(User.builder()
										.id("456")
										.userName("tester")
										.build()
								)
								.build(),
						GlobalUserPermission.builder()
								.id("abc")
								.scope(READ_USER_PROFILE)
								.owner(User.builder()
										.id("456")
										.userName("tester")
										.build()
								)
								.build()
				)
		);
		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionQueryService.getInboundGlobalUserPermissions("123");

		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.scope(FIND_AS_COACH)
						.owner(User.builder()
								.id("456")
								.userName("tester")
								.build()
						)
						.build(),
				GlobalUserPermission.builder()
						.id("abc")
						.scope(READ_USER_PROFILE)
						.owner(User.builder()
								.id("456")
								.userName("tester")
								.build()
						)
						.build()
		);
	}

	@DisplayName("Throws exception if owner ID is null when getting user inbound global permissions.")
	@Test
	void throwExceptionIfUserIdIsNullWhenGettingInboundGlobalUserPermissions() {
		assertThrows(IllegalArgumentException.class, () -> globalUserPermissionQueryService.getInboundGlobalUserPermissions(null));
	}

}
