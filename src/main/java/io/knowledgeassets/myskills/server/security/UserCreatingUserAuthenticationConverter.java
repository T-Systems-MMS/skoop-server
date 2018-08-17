package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Slf4j
public class UserCreatingUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final Collection<? extends GrantedAuthority> defaultAuthorities;

	public UserCreatingUserAuthenticationConverter(UserQueryService userQueryService,
												   UserCommandService userCommandService,
												   Set<String> defaultAuthorities) {
		this.userQueryService = userQueryService;
		this.userCommandService = userCommandService;
		this.defaultAuthorities = AuthorityUtils.createAuthorityList(defaultAuthorities.toArray(new String[]{}));
	}

	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (!map.containsKey(USERNAME)) {
			log.warn("Claim '{}' not found in token", USERNAME);
			return null;
		}
		if (!(map.get(USERNAME) instanceof String)) {
			log.warn("Claim '{}' must be a String", USERNAME);
			return null;
		}
		String userName = (String) map.get(USERNAME);
		Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
		// TODO: Extract first name, last name, etc. from claim map and create user with this additional data.
		User user = userQueryService.getUserByUserName(userName)
				.orElseGet(() -> userCommandService.createUser(userName));
		UserIdentity principal = new UserIdentity(user.getId(), user.getUserName(), user.getFirstName(),
				user.getLastName(), user.getEmail(), "N/A", true, true,
				true, true, authorities);
		return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
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
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
