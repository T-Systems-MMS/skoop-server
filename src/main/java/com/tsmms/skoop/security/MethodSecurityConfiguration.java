package com.tsmms.skoop.security;

import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	private final UserPermissionQueryService userPermissionQueryService;

	public MethodSecurityConfiguration(UserPermissionQueryService userPermissionQueryService) {
		this.userPermissionQueryService = userPermissionQueryService;
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new SkoopMethodSecurityExpressionHandler(userPermissionQueryService);
	}
}
