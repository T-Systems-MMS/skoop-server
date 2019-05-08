package com.tsmms.skoop.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static com.tsmms.skoop.user.GlobalUserPermissionScope.*;

@DataNeo4jTest
class GlobalUserPermissionRepositoryTests {

	@Autowired
	private GlobalUserPermissionRepository globalUserPermissionRepository;

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

		globalUserPermissionRepository.saveAll(Arrays.asList(
				GlobalUserPermission.builder()
						.id("def")
						.owner(tester)
						.scope(READ_USER_PROFILE)
						.build(),
				GlobalUserPermission.builder()
						.id("abc")
						.owner(owner)
						.scope(READ_USER_PROFILE)
						.build()
		));

		final Stream<GlobalUserPermission> globalPermissions = globalUserPermissionRepository.findByOwnerId("123");
		assertThat(globalPermissions).containsExactlyInAnyOrder(
				GlobalUserPermission.builder()
						.id("abc")
						.owner(owner)
						.scope(READ_USER_PROFILE)
						.build()
		);
	}

	@DisplayName("Check if global permission granted.")
	@Test
	void isGlobalPermissionGranted() {
		globalUserPermissionRepository.save(GlobalUserPermission.builder()
				.id("abc")
				.owner(User.builder()
						.id("123")
						.userName("owner")
						.build())
				.scope(READ_USER_PROFILE)
				.build()
		);
		assertThat(globalUserPermissionRepository.isGlobalPermissionGranted("123", READ_USER_PROFILE)).isTrue();
	}

}
