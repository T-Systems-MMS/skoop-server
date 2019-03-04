package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTests {

	@Mock
	private CommunityQueryService communityQueryService;

	private SecurityService securityService;

	@BeforeEach
	void setUp() {
		this.securityService = new SecurityService(communityQueryService);
	}

	@DisplayName("The user has community manager role.")
	@Test
	void userHasCommunityManagerRole() {
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(true);
		final Map<String, Object> claims = new HashMap<>();
		claims.put(MYSKILLS_USER_ID, "123");
		final Jwt jwt = new Jwt("token",
				LocalDateTime.of(2019, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2019, 1, 2, 10, 0).toInstant(ZoneOffset.UTC),
				Collections.singletonMap("key", "value"),
				claims);
		assertThat(securityService.hasCommunityManagerRole(jwt, "456")).isTrue();
	}

	@DisplayName("The user does not have community manager role.")
	@Test
	void userDoesNotHaveCommunityManagerRole() {
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(false);
		final Map<String, Object> claims = new HashMap<>();
		claims.put(MYSKILLS_USER_ID, "123");
		final Jwt jwt = new Jwt("token",
				LocalDateTime.of(2019, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2019, 1, 2, 10, 0).toInstant(ZoneOffset.UTC),
				Collections.singletonMap("key", "value"),
				claims);
		assertThat(securityService.hasCommunityManagerRole(jwt, "456")).isFalse();
	}

	@DisplayName("Throws an exception when user identifier is not defined.")
	@Test
	void throwsExceptionWhenUserIdentifierIsNotDefined() {
		final Map<String, Object> claims = new HashMap<>();
		claims.put(MYSKILLS_USER_ID, "");
		final Jwt jwt = new Jwt("token",
				LocalDateTime.of(2019, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2019, 1, 2, 10, 0).toInstant(ZoneOffset.UTC),
				Collections.singletonMap("key", "value"),
				claims);
		assertThrows(IllegalArgumentException.class, () -> securityService.hasCommunityManagerRole(jwt, "456"));
	}

}
