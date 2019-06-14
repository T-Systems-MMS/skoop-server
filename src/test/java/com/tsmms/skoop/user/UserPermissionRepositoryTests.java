package com.tsmms.skoop.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class UserPermissionRepositoryTests {

	@Autowired
	private UserPermissionRepository userPermissionRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Finds user permissions granted to the user with a specified user id.")
	void findsUserPermissionsGrantedToUserWithSpecifiedUserId() {
		final User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();
		userRepository.save(owner);

		final User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();
		userRepository.save(authorizedUser);

		final UserPermission userPermission = UserPermission.builder()
				.owner(owner)
				.authorizedUsers(Collections.singletonList(authorizedUser))
				.scope(UserPermissionScope.READ_USER_SKILLS)
				.id("ABC")
				.build();
		userPermissionRepository.save(userPermission);

		final Iterable<UserPermission> userPermissions = userPermissionRepository.findByAuthorizedUsersId("456");
		assertThat(userPermissions).hasSize(1);
		assertThat(userPermissions).containsExactlyInAnyOrder(userPermission);
	}

	@DisplayName("Deletes user permissions by owner ID and scope being in the specified collection.")
	@Test
	void deleteByOwnerIdAndScopeIn() {
		final User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();
		userRepository.save(owner);

		final User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();
		userRepository.save(authorizedUser);

		userPermissionRepository.saveAll(Arrays.asList(
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_SKILLS)
						.id("ABC")
						.build(),
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.id("DEF")
						.build()
		));
		assertThat(userPermissionRepository.count()).isEqualTo(2L);
		userPermissionRepository.deleteByOwnerIdAndScope(owner.getId(), UserPermissionScope.READ_USER_SKILLS);
		assertThat(userPermissionRepository.count()).isEqualTo(1L);
	}

	@DisplayName("Finds by owner ID and scope.")
	@Test
	void findsByOwnerAndScope() {

		final User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();
		userRepository.save(owner);

		final User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();
		userRepository.save(authorizedUser);

		userPermissionRepository.saveAll(Arrays.asList(
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_SKILLS)
						.id("ABC")
						.build(),
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.id("DEF")
						.build()
		));

		assertThat(userPermissionRepository.findByOwnerIdAndScope("123", UserPermissionScope.READ_USER_SKILLS))
				.containsExactlyInAnyOrder(
						UserPermission.builder()
								.owner(owner)
								.authorizedUsers(Collections.singletonList(authorizedUser))
								.scope(UserPermissionScope.READ_USER_SKILLS)
								.id("ABC")
								.build()
				);
	}

	@DisplayName("Finds by authorized user ID and scope.")
	@Test
	void findsByAuthorizedUsersIdAndScope() {
		final User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();
		userRepository.save(owner);

		final User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();
		userRepository.save(authorizedUser);

		userPermissionRepository.saveAll(Arrays.asList(
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_SKILLS)
						.id("ABC")
						.build(),
				UserPermission.builder()
						.owner(owner)
						.authorizedUsers(Collections.singletonList(authorizedUser))
						.scope(UserPermissionScope.READ_USER_PROFILE)
						.id("DEF")
						.build()
		));

		assertThat(userPermissionRepository.findByAuthorizedUsersIdAndScope("456", UserPermissionScope.READ_USER_SKILLS))
				.containsExactlyInAnyOrder(
						UserPermission.builder()
								.owner(owner)
								.authorizedUsers(Collections.singletonList(authorizedUser))
								.scope(UserPermissionScope.READ_USER_SKILLS)
								.id("ABC")
								.build()
				);
	}

}
