package io.knowledgeassets.myskills.server.security;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.InputStream;
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
	void rolesAreExtractedAsGrantedAuthoritiesBothFromRealmAndResourceAccess() throws Exception {
		final ClassPathResource body = new ClassPathResource("roles_as_part_of_jwt.json");
		try (final InputStream is = body.getInputStream()) {
			JSONObject claims = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(is, JSONObject.class);
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

}
