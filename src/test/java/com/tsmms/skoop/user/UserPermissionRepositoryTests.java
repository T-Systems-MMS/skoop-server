package com.tsmms.skoop.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

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
		User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();
		userRepository.save(owner);

		User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();
		userRepository.save(authorizedUser);

		UserPermission userPermission = UserPermission.builder()
				.owner(owner)
				.authorizedUsers(Collections.singletonList(authorizedUser))
				.scope(UserPermissionScope.READ_USER_SKILLS)
				.id("ABC")
				.build();
		userPermissionRepository.save(userPermission);

		Iterable<UserPermission> userPermissions = userPermissionRepository.findByAuthorizedUsersId("456");
		assertThat(userPermissions).hasSize(1);
		assertThat(userPermissions).containsExactlyInAnyOrder(userPermission);
	}

}
