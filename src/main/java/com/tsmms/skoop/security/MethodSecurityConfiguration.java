package com.tsmms.skoop.security;

import com.tsmms.skoop.user.query.GlobalUserPermissionQueryService;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	private final UserPermissionQueryService userPermissionQueryService;
	private final GlobalUserPermissionQueryService globalUserPermissionQueryService;

	public MethodSecurityConfiguration(UserPermissionQueryService userPermissionQueryService,
									   GlobalUserPermissionQueryService globalUserPermissionQueryService) {
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
		this.globalUserPermissionQueryService = requireNonNull(globalUserPermissionQueryService);
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new SkoopMethodSecurityExpressionHandler(userPermissionQueryService, globalUserPermissionQueryService);
	}
}
