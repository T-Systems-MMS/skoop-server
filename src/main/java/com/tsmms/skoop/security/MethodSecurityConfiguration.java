package com.tsmms.skoop.security;

import com.tsmms.skoop.user.query.UserGlobalPermissionQueryService;
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
	private final UserGlobalPermissionQueryService userGlobalPermissionQueryService;

	public MethodSecurityConfiguration(UserPermissionQueryService userPermissionQueryService,
									   UserGlobalPermissionQueryService userGlobalPermissionQueryService) {
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
		this.userGlobalPermissionQueryService = requireNonNull(userGlobalPermissionQueryService);
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new SkoopMethodSecurityExpressionHandler(userPermissionQueryService, userGlobalPermissionQueryService);
	}
}
