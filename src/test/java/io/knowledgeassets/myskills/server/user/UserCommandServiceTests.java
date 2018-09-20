package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTests {
	@Mock
	private UserRepository userRepository;
	private UserCommandService userCommandService;

	@BeforeEach
	void setUp() {
		userCommandService = new UserCommandService(userRepository);
	}

	@Test
	@DisplayName("Create User")
	void createUser() {
		given(userRepository.findByUserName("tester1")).willReturn(Optional.empty());
		given(userRepository.save(ArgumentMatchers.isA(User.class)))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(false).build());

		User user = userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com");

		assertThat(user).isNotNull();
		assertThat(user.getId()).isNotNull();
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester1");
		assertThat(user.getEmail()).isEqualTo("tester1@gmail.com");
		assertThat(user.getCoach()).isFalse();
	}

	@Test
	@DisplayName("Create User that has already exist")
	void createUser_ThrowsException() {
		given(userRepository.findByUserName("tester1")).willReturn(Optional.of(
				User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build()));

		assertThrows(IllegalArgumentException.class, () -> {
			userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com");
		});
	}

	@Test
	@DisplayName("Update User")
	void updateUser() {
		given(userRepository.findById("123"))
				.willReturn(Optional.of(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build()));
		given(userRepository.save(ArgumentMatchers.any(User.class)))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(true).build());

		User user = userCommandService.updateUser("123", UserRequest.builder().userName("tester1")
				.firstName("firstTester").lastName(null).email("tester1@gmail.com").coach(true).build());

		assertThat(user).isNotNull();
		assertThat(user.getId()).isNotNull();
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester1");
		assertThat(user.getEmail()).isEqualTo("tester1@gmail.com");
		assertThat(user.getCoach()).isTrue();
	}

}
