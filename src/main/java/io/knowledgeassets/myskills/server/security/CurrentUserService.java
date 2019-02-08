package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.UserNotAuthenticatedException;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static io.knowledgeassets.myskills.server.exception.enums.Model.USER;
import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;

@Service
public class CurrentUserService {

	private final UserQueryService userQueryService;

	public CurrentUserService(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	public User getCurrentUser() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			throw new UserNotAuthenticatedException();
		}

		if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt)) {
			throw new UserNotAuthenticatedException();
		}
		final String userId = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString(MYSKILLS_USER_ID);
		return userQueryService.getUserById(userId).orElseThrow(() -> NoSuchResourceException.builder()
				.model(USER)
				.searchParamsMap(new String[]{"id", userId})
				.build());
	}

}
