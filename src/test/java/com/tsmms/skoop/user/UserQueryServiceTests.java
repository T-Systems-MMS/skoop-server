package com.tsmms.skoop.user;

import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTests {

	@Mock
	private UserRepository userRepository;

	private UserQueryService userQueryService;

	@BeforeEach
	void setUp() {
		userQueryService = new UserQueryService(userRepository);
	}

	@Test
	@DisplayName("Returns a stream of all users from the data repository")
	void returnsStreamOfAllUsers() {
		given(userRepository.findAll()).willReturn(Arrays.asList(
				User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build(),
				User.builder().id("456").userName("tester2").firstName("secondTester").email("tester2@gmail.com").build()));

		Stream<User> users = userQueryService.getUsers();

		assertThat(users).isNotNull();
		List<User> userList = users.collect(toList());
		assertThat(userList).hasSize(2);
		User user = userList.get(0);
		assertThat(user.getId()).isEqualTo("123");
		assertThat(user.getUserName()).isEqualTo("tester1");
		assertThat(user.getFirstName()).isEqualTo("firstTester");
		user = userList.get(1);
		assertThat(user.getId()).isEqualTo("456");
		assertThat(user.getUserName()).isEqualTo("tester2");
		assertThat(user.getEmail()).isEqualTo("tester2@gmail.com");
	}

	@DisplayName("Gets user subordinates.")
	@Test
	void getUserSubordinates() {
		final User manager = userRepository.save(User.builder()
				.id("123")
				.userName("manager")
				.build()
		);
		given(userRepository.findByManagerId("123")).willReturn(Stream.of(
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
		assertThat(userQueryService.getUserSubordinates("123")).containsExactlyInAnyOrder(
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

	@DisplayName("Throws exception when getting user subordinates if user ID is null.")
	@Test
	void throwsExceptionWhenGettingUserSubordinatesIfUserIdIsNull() {
		assertThrows(IllegalArgumentException.class, () -> userQueryService.getUserSubordinates(null));
	}

}
