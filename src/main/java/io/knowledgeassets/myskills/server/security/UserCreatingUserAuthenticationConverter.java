package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
public class UserCreatingUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final Collection<? extends GrantedAuthority> defaultAuthorities;
	private final String CLAIM_EMAIL = "email";
	private final String CLAIM_FIRST_NAME = "given_name";
	private final String CLAIM_LAST_NAME = "family_name";

	public UserCreatingUserAuthenticationConverter(UserQueryService userQueryService,
												   UserCommandService userCommandService,
												   Set<String> defaultAuthorities) {
		this.userQueryService = userQueryService;
		this.userCommandService = userCommandService;
		this.defaultAuthorities = AuthorityUtils.createAuthorityList(defaultAuthorities.toArray(new String[]{}));
	}

	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		User userToCreate = createUserFromTokenClaims(map);
		if (userToCreate == null) return null;

		Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
		User user;
		try {
			user = userQueryService.getByUserName(userToCreate.getUserName())
					.orElseGet(() -> userCommandService.createUser(userToCreate.getUserName(),
							userToCreate.getFirstName(), userToCreate.getLastName(), userToCreate.getEmail()));
		} catch (ClientException e) {
			// Retry user lookup since creation my have failed due to concurrent creation by another request.
			user = userQueryService.getByUserName(userToCreate.getUserName())
					.orElseThrow(() -> new IllegalStateException(format(
							"User with user name '%s' was not found after retry", userToCreate.getUserName())));
		}
		UserIdentity principal = UserIdentity.of(user, authorities);
		return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
	}

	private User createUserFromTokenClaims(Map<String, ?> map) {
		if (!map.containsKey(USERNAME)) {
			log.warn("Claim '{}' not found in token", USERNAME);
			return null;
		}
		if (!(map.get(USERNAME) instanceof String)) {
			log.warn("Claim '{}' must be a String", USERNAME);
			return null;
		}
		String userName = (String) map.get(USERNAME);
		String email = (String) map.get(CLAIM_EMAIL);
		String firstName = (String) map.get(CLAIM_FIRST_NAME);
		String lastName = (String) map.get(CLAIM_LAST_NAME);

		return User.builder()
				.userName(userName)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.build();
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
					.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw InvalidInputException.builder().message("Authorities must be either a String or a Collection").build();
	}
}
