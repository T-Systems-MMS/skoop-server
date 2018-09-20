package io.knowledgeassets.myskills.server.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
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

}
