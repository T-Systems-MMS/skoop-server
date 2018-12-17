package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Map;

import static io.knowledgeassets.myskills.server.security.JwtClaims.*;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class UserClaimSetConverter implements Converter<Map<String, Object>, Map<String, Object>> {
	private final Converter<Map<String, Object>, Map<String, Object>> defaultConverter =
			MappedJwtClaimSetConverter.withDefaults(emptyMap());
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;

	public UserClaimSetConverter(UserQueryService userQueryService, UserCommandService userCommandService) {
		this.userQueryService = userQueryService;
		this.userCommandService = userCommandService;
	}

	@Override
	public Map<String, Object> convert(Map<String, Object> claims) {
		Map<String, Object> mappedClaims = defaultConverter.convert(claims);
		if (mappedClaims == null) {
			throw new JwtValidationException("No default claims found in bearer token",
					singletonList(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
							"Default claims are missing", null)));
		}

		// Claim 'user_name' is required to lookup the user by its name in the database.
		String userName = (String) mappedClaims.get(USER_NAME);
		if (isBlank(userName)) {
			throw new JwtValidationException(format("Claim '%s' is required in bearer token", USER_NAME),
					singletonList(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
							"Claim '" + USER_NAME + "' is missing", null)));
		}

		// Additional user data is taken from the JWT to initialize the user in the database.
		String firstName = (String) mappedClaims.get(FIRST_NAME);
		String lastName = (String) mappedClaims.get(LAST_NAME);
		String email = (String) mappedClaims.get(EMAIL);

		User user = loadUser(userName, firstName, lastName, email);

		// Add internal user ID as additional claim to be used by authorization framework.
		mappedClaims.put(MYSKILLS_USER_ID, user.getId());

		return mappedClaims;
	}

	private User loadUser(String userName, String firstName, String lastName, String email) {
		User user;
		try {
			user = userQueryService.getByUserName(userName)
					.orElseGet(() -> userCommandService.createUser(userName, firstName, lastName, email));
		} catch (ClientException e) {
			// Retry user lookup because creation may have failed due to concurrent creation by another request.
			user = userQueryService.getByUserName(userName)
					.orElseThrow(() -> new IllegalStateException(format(
							"User with user name '%s' was not found after retry", userName)));
		}
		return user;
	}
}
