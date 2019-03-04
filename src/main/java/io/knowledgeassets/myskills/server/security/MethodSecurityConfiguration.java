package io.knowledgeassets.myskills.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	private final SecurityService securityService;

	public MethodSecurityConfiguration(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new MySkillsMethodSecurityExpressionHandler(securityService);
	}
}
