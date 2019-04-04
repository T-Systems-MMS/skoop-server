package com.tsmms.skoop.security;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.UserNotAuthenticatedException;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static com.tsmms.skoop.exception.enums.Model.USER;
import static com.tsmms.skoop.security.JwtClaims.SKOOP_USER_ID;

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
		final String userId = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString(SKOOP_USER_ID);
		return userQueryService.getUserById(userId).orElseThrow(() -> NoSuchResourceException.builder()
				.model(USER)
				.searchParamsMap(new String[]{"id", userId})
				.build());
	}

}
