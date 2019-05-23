package com.tsmms.skoop.scheduling;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ActiveDirectoryServiceMockTests {

	@Mock
	private UserQueryService userQueryService;

	private ActiveDirectoryServiceMock activeDirectoryServiceMock;

	@BeforeEach
	void setUp() {
		activeDirectoryServiceMock = new ActiveDirectoryServiceMock(userQueryService);
	}

	@DisplayName("Gets manager username of the specified user.")
	@Test
	void getManagerByUser() {
		given(userQueryService.getUsers()).willReturn(
				Stream.of(
						User.builder()
								.id("123")
								.userName("tester")
								.build(),
						User.builder()
								.id("456")
								.userName("anotherTester")
								.build()
				)
		);
		assertThat(activeDirectoryServiceMock.getManagerByUser("tester")).isEqualTo(Optional.of("anotherTester"));
	}

	@DisplayName("Throws exception when getting manager by user if username is null.")
	@Test
	void throwExceptionWhenGettingManagerByUserIfUsernameIsNull() {
		assertThrows(IllegalArgumentException.class, () -> activeDirectoryServiceMock.getManagerByUser(null));
	}

}
