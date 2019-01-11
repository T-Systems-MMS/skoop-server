package io.knowledgeassets.myskills.server.security;


import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MySkillsJwtAuthenticationConverterTests {

	private MySkillsJwtAuthenticationConverter mySkillsJwtAuthenticationConverter;

	@BeforeEach
	void setUp() {
		mySkillsJwtAuthenticationConverter = new MySkillsJwtAuthenticationConverter();
	}

	@Test
	@DisplayName("Roles are extracted as granted authorities both from realm and resource access.")
	void rolesAreExtractedAsGrantedAuthoritiesBothFromRealmAndResourceAccess() {
		JSONObject claims = new JSONObject();
		claims.put("azp", "myskills");
		JSONObject resourceAccess = new JSONObject();
		JSONObject mySkills = new JSONObject();
		JSONArray roles = new JSONArray();
		roles.add("ADMIN");
		roles.add("USER");
		mySkills.put("roles", roles);
		resourceAccess.put("myskills", mySkills);
		claims.put("resource_access", resourceAccess);
		JSONObject realmAccess = new JSONObject();
		JSONArray realmRoles = new JSONArray();
		realmRoles.add("REALM");
		realmAccess.put("roles", realmRoles);
		claims.put("realm_access", realmAccess);
		Collection<GrantedAuthority> grantedAuthorities = mySkillsJwtAuthenticationConverter.extractAuthorities(new Jwt("token",
				LocalDateTime.of(2019, 1, 1, 10, 0).toInstant(ZoneOffset.UTC),
				LocalDateTime.of(2019, 1, 2, 10, 0).toInstant(ZoneOffset.UTC),
				Collections.singletonMap("key", "value"),
				claims));
		assertThat(grantedAuthorities).hasSize(3);
		assertThat(grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER", "ROLE_REALM");
	}

}
