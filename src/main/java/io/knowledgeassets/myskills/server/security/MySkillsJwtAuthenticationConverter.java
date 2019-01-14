package io.knowledgeassets.myskills.server.security;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * JWT authentication converter that extracts roles from a token.
 */
public class MySkillsJwtAuthenticationConverter extends JwtAuthenticationConverter {

	private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

	private static final Collection<String> KNOWN_SCOPE_ATTRIBUTE_NAMES =
			Arrays.asList("resource_access", "realm_access");

	@Override
	protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		final Collection<GrantedAuthority> grantedAuthorities = super.extractAuthorities(jwt);
		// Authorized party will be a client in case of keycloak
		final Object authorizedPartyObject = jwt.getClaims().get("azp");
		if (authorizedPartyObject instanceof String) {
			final String authorizedParty = (String) authorizedPartyObject;
			grantedAuthorities.addAll(extractRoles(jwt, authorizedParty));
		}
		return grantedAuthorities;
	}

	private Collection<GrantedAuthority> extractRoles(Jwt jwt, String authorizedParty) {
		return this.getRoles(jwt, authorizedParty)
				.stream()
				.map(authority -> ROLE_AUTHORITY_PREFIX + authority)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	private Collection<String> getRoles(Jwt jwt, String authorizedParty) {
		final Set<String> roles = new TreeSet<>();
		for (String attributeName : KNOWN_SCOPE_ATTRIBUTE_NAMES) {
			final Object attribute = jwt.getClaims().get(attributeName);
			if (attribute instanceof JSONObject) {
				final JSONObject attributeObject = (JSONObject) attribute;
				final Object authorizedPartyObject = attributeObject.get(authorizedParty);
				if (authorizedPartyObject instanceof JSONObject) {
					processRoles(roles, (JSONObject) authorizedPartyObject);
				}
				else {
					processRoles(roles, attributeObject);
				}
			}
		}
		return roles;
	}

	private void processRoles(Set<String> roles, JSONObject object) {
		final Object roleList = object.get("roles");
		if (roleList instanceof JSONArray) {
			final JSONArray o3 = (JSONArray) roleList;
			roles.addAll(Arrays.stream(o3.toArray()).map(Object::toString).collect(Collectors.toSet()));
		}
	}
}
