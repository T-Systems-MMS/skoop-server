package com.tsmms.skoop.scheduling;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.command.UserCommandService;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserReconciliationJobTests {

	@Mock
	private UserQueryService userQueryService;

	@Mock
	private UserCommandService userCommandService;

	@Mock
	private ActiveDirectoryServiceMock activeDirectoryServiceMock;

	private UserReconciliationJob userReconciliationJob;

	@BeforeEach
	void setUp() {
		this.userReconciliationJob = new UserReconciliationJob(
				userQueryService,
				userCommandService,
				activeDirectoryServiceMock
		);
	}

	@DisplayName("Run the user reconciliation job.")
	@Test
	void runJob() {
		given(userQueryService.getUsers()).willReturn(Stream.of(
				User.builder()
				.id("123")
				.userName("tester")
				.build(),
				User.builder()
				.id("456")
				.userName("anotherTester")
				.build()
		));
		given(activeDirectoryServiceMock.getManagerByUser(anyString())).willReturn(Optional.of("manager"));
		assertDoesNotThrow(() -> userReconciliationJob.run());
	}

}
