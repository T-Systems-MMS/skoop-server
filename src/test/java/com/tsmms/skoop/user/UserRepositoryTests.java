package com.tsmms.skoop.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class UserRepositoryTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Provides the existing user queried by its exact name")
	void providesUserByUserName() {
		// Given
		userRepository.save(User.builder().id("123").userName("tester")
				.firstName("tester1").email("tester1@gmail.com").build());
		// When
		Optional<User> user = userRepository.findByUserName("tester");
		// Then
		assertThat(user).isNotEmpty();
		assertThat(user.get().getId()).isEqualTo("123");
		assertThat(user.get().getUserName()).isEqualTo("tester");
		assertThat(user.get().getLastName()).isNull();
	}

	@DisplayName("Gets user subordinates.")
	@Test
	void getSubordinates() {
		final User manager = userRepository.save(User.builder()
				.id("123")
				.userName("manager")
				.build()
		);
		userRepository.saveAll(Arrays.asList(
				User.builder()
						.id("456")
						.userName("tester")
						.manager(manager)
						.build(),
				User.builder()
						.id("789")
						.userName("anotherTester")
						.manager(manager)
						.build()
				)
		);
		assertThat(userRepository.findByManagerId(manager.getId())).containsExactlyInAnyOrder(
				User.builder()
						.id("456")
						.userName("tester")
						.manager(manager)
						.build(),
				User.builder()
						.id("789")
						.userName("anotherTester")
						.manager(manager)
						.build()
		);
	}

}
