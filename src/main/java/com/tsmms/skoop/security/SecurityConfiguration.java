package com.tsmms.skoop.security;

import com.tsmms.skoop.user.command.UserCommandService;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final String jwtIssuerUri;
	private final String jwtJwkSetUri;
	private final String[] publicResourcesPatterns;

	public SecurityConfiguration(@Value("${security.oauth2.resourceserver.jwt.issuer-uri:}") String jwtIssuerUri,
								 @Value("${security.oauth2.resourceserver.jwt.jwk-set-uri:}") String jwtJwkSetUri,
								 @Value("${springfox.documentation.swagger.v2.path}") String apiSpecPath) {
		this.jwtIssuerUri = jwtIssuerUri;
		this.jwtJwkSetUri = jwtJwkSetUri;
		this.publicResourcesPatterns = new String[]{
				"/",
				"/favicon.ico",
				"/csrf",
				"/error",
				"/swagger-ui.html",
				"/webjars/**",
				"/swagger-resources/**",
				apiSpecPath
		};
	}

	@Bean
	public JwtDecoder jwtDecoder(UserQueryService userQueryService, UserCommandService userCommandService) {
		NimbusJwtDecoderJwkSupport jwtDecoder;
		if (isNotBlank(jwtIssuerUri)) {
			jwtDecoder = (NimbusJwtDecoderJwkSupport) JwtDecoders.fromOidcIssuerLocation(jwtIssuerUri);
		} else if (isNotBlank(jwtJwkSetUri)) {
			jwtDecoder = new NimbusJwtDecoderJwkSupport(jwtJwkSetUri);
		} else {
			throw new BeanCreationException("Either a JWT issuer URI or a JWK set URI must be configured");
		}
		jwtDecoder.setClaimSetConverter(new UserClaimSetConverter(userQueryService, userCommandService));
		return jwtDecoder;
	}

	@Bean
	public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
		return new SkoopJwtAuthenticationConverter();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(publicResourcesPatterns).permitAll()
				.anyRequest().authenticated()
				.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().oauth2ResourceServer().jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());
	}
}
