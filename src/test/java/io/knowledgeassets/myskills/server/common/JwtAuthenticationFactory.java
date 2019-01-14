package io.knowledgeassets.myskills.server.common;

import io.knowledgeassets.myskills.server.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;
import static java.time.Instant.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;

public class JwtAuthenticationFactory {
	private static final String ROLE_PREFIX = "ROLE_";
	private static final String JWT_DUMMY_VALUE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.FVetq2I3kjfxWqOG3G3E2zWULY8ueQMakSOY7G5qaOs";
	private static final Map<String, Object> JWT_HEADERS = Map.of("alg", "HS256", "typ", "JWT");

	public static Authentication withUser(User user, String... roles) {
		Jwt jwt = new Jwt(JWT_DUMMY_VALUE, now(), now().plusSeconds(60), JWT_HEADERS,
				singletonMap(MYSKILLS_USER_ID, user.getId()));
		if (roles.length == 0) {
			return new JwtAuthenticationToken(jwt, emptyList());
		}
		else {
			final List<GrantedAuthority> authorities = new ArrayList<>(
					roles.length);
			for (String role : roles) {
				if (role.startsWith(ROLE_PREFIX)) {
					throw new IllegalArgumentException(
							"Role should not start with " + ROLE_PREFIX
									+ " since this method automatically prefixes with this value. Got "
									+ role);
				} else {
					authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
				}
			}
			return new JwtAuthenticationToken(jwt, authorities);
		}
	}
}
