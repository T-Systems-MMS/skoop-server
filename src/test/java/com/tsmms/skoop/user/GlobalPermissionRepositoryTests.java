package com.tsmms.skoop.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class GlobalPermissionRepositoryTests {

	@Autowired
	private GlobalPermissionRepository globalPermissionRepository;

	@DisplayName("Finds global permissions by owner ID.")
	@Test
	void findByOwnerId() {
		User owner = User.builder()
				.id("123")
				.userName("owner")
				.build();

		User tester = User.builder()
				.id("456")
				.userName("tester")
				.build();

		globalPermissionRepository.saveAll(Arrays.asList(
				GlobalPermission.builder()
						.id("abc")
						.owner(tester)
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.build(),
				GlobalPermission.builder()
						.id("abc")
						.owner(owner)
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.build()
		));

		final Stream<GlobalPermission> globalPermissions = globalPermissionRepository.findByOwnerId("123");
		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalPermission.builder()
						.id("abc")
						.owner(owner)
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.build()
		);
	}

	@DisplayName("Check if global permission granted.")
	@Test
	void isGlobalPermissionGranted() {
		globalPermissionRepository.save(GlobalPermission.builder()
				.id("abc")
				.owner(User.builder()
						.id("123")
						.userName("owner")
						.build())
				.scope(UserPermissionScope.READ_USER_PROFILE)
				.build()
		);
		assertThat(globalPermissionRepository.isGlobalPermissionGranted("123", UserPermissionScope.READ_USER_PROFILE)).isTrue();
	}

}
