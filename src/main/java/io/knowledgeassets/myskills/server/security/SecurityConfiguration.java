package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Properties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated()
				.and().httpBasic()
				// TODO: Use cookie-based CSRF token repository for production
//				.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Exclude paths related to Swagger UI from Spring Security filter chain to allow public access to API docs.
		web.ignoring().antMatchers(
				"/",
				"/favicon.ico",
				"/csrf",
				"/error",
				"/swagger-ui.html",
				"/webjars/**",
				"/swagger-resources/**",
				"/api-spec");
	}

	@Bean
	public UserDetailsService userDetailsService(UserQueryService userQueryService,
												 UserCommandService userCommandService,
												 MySkillsProperties properties) {
		// Configure preliminary users from given application settings (see application.yml).
		Properties users = new Properties();
		properties.getSecurity().getUsers().forEach(userEntry -> {
			int userNameDelimiter = userEntry.indexOf(',');
			String userName = userEntry.substring(0, userNameDelimiter);
			String userSettings = userEntry.substring(userNameDelimiter + 1);
			users.setProperty(userName, userSettings);
			log.info("Configuring user '{}' with settings: {}", userName, userSettings);
		});
		return new UserCreatingUserDetailsService(users, userQueryService, userCommandService);
	}
}
