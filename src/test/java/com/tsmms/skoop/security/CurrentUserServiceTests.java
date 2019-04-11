package com.tsmms.skoop.security;

import com.tsmms.skoop.exception.UserNotAuthenticatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrentUserServiceTests {

	private CurrentUserService currentUserService;

	@BeforeEach
	void setUp() {
		currentUserService = new CurrentUserService();
	}

	@DisplayName("Authenticated user ID is the ID of the authenticated user.")
	@Test
	void isAuthenticatedUserIdEqualToIdOfAuthenticatedUser() {
		assertThrows(UserNotAuthenticatedException.class, () -> currentUserService.getCurrentUserId());
	}

	@DisplayName("Authenticated user ID is the expected one.")
	@Test
	void isAuthenticatedUserIdEqualToExpectedId() {
		assertThat(currentUserService.isAuthenticatedUserId("123", "123")).isTrue();
	}

}
