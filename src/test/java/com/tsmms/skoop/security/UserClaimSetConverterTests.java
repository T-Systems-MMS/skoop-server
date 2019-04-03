package com.tsmms.skoop.security;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.command.UserCommandService;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserClaimSetConverterTests {

	@Mock
	private UserQueryService userQueryService;
	@Mock
	private UserCommandService userCommandService;
	private UserClaimSetConverter userClaimSetConverter;

	@BeforeEach
	void setUp() {
		this.userClaimSetConverter = new UserClaimSetConverter(userQueryService, userCommandService);
	}

	@Test
	@DisplayName("User claim set is enriched with a user id when the user exists.")
	void testIfUserClaimSetIsEnrichedWithUserIdWhenUserExists() {
		given(userQueryService.getByUserName("johndoe")).willReturn(Optional.of(
				User.builder()
						.id("123")
						.userName("johndoe")
						.build()
		));
		final Map<String, Object> claims = new HashMap<>();
		claims.put("given_name", "John");
		claims.put("family_name", "Doe");
		claims.put("email", "johndoe@mail.com");
		claims.put("user_name", "johndoe");
		claims.put("jti", "38929539-bbac-4d4a-be9b-9ba2305f0f51");
		claims.put("session_state", "355b7a43-ef3a-4e85-9e95-cde240e47d56");
		final Map<String, Object> enrichedClaims = userClaimSetConverter.convert(claims);
		assertThat(enrichedClaims).containsKeys("skoop_user_id");
		assertThat(enrichedClaims).containsValues("123");
	}

	@Test
	@DisplayName("User claim set is enriched with a user id when the user does not exist.")
	void testIfUserClaimSetIsEnrichedWithUserIdWhenUserDoesNotExist() {
		given(userQueryService.getByUserName("johndoe")).willReturn(Optional.empty());
		given(userCommandService.createUser("johndoe", "John", "Doe", "johndoe@mail.com")).willReturn(
				User.builder()
						.id("123")
						.userName("johndoe")
						.firstName("John")
						.lastName("Doe")
						.email("johndoe@mail.com")
						.build());
		final Map<String, Object> claims = new HashMap<>();
		claims.put("given_name", "John");
		claims.put("family_name", "Doe");
		claims.put("email", "johndoe@mail.com");
		claims.put("user_name", "johndoe");
		claims.put("jti", "38929539-bbac-4d4a-be9b-9ba2305f0f51");
		claims.put("session_state", "355b7a43-ef3a-4e85-9e95-cde240e47d56");
		final Map<String, Object> enrichedClaims = userClaimSetConverter.convert(claims);
		assertThat(enrichedClaims).containsKeys("skoop_user_id");
		assertThat(enrichedClaims).containsValues("123");
	}

}
