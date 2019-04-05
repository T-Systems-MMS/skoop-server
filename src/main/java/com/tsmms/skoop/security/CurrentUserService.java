package com.tsmms.skoop.security;

import com.tsmms.skoop.exception.UserNotAuthenticatedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static com.tsmms.skoop.security.JwtClaims.SKOOP_USER_ID;

@Service
public class CurrentUserService {

	public String getCurrentUserId() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			throw new UserNotAuthenticatedException();
		}

		if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt)) {
			throw new UserNotAuthenticatedException();
		}
		return  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString(SKOOP_USER_ID);
	}

	/**
	 * Checks whether the given user ID equals the user ID of the authenticated user.
	 *
	 * @param userId User ID to check against the authenticated user.
	 * @return <code>true</code> if the given user ID equals the user ID of the authenticated user.
	 */
	public boolean isAuthenticatedUserId(String userId) {
		return isAuthenticatedUserId(getCurrentUserId(), userId);
	}
	/**
	 * Checks whether the given user ID equals the user ID of the authenticated user.
	 *
	 * @param authenticatedUserId User ID of the authenticated user.
	 * @param userId User ID to check against the authenticated user.
	 * @return <code>true</code> if the given user ID equals the user ID of the authenticated user.
	 */
	public boolean isAuthenticatedUserId(String authenticatedUserId, String userId) {
		return userId == null && authenticatedUserId == null || userId != null && userId.equals(authenticatedUserId);
	}

}
