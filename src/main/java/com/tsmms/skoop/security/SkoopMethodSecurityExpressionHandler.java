package com.tsmms.skoop.security;

import com.tsmms.skoop.user.query.UserGlobalPermissionQueryService;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import static java.util.Objects.requireNonNull;

public class SkoopMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private final UserPermissionQueryService userPermissionQueryService;
	private final UserGlobalPermissionQueryService userGlobalPermissionQueryService;

	public SkoopMethodSecurityExpressionHandler(UserPermissionQueryService userPermissionQueryService,
												UserGlobalPermissionQueryService userGlobalPermissionQueryService) {
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
		this.userGlobalPermissionQueryService = requireNonNull(userGlobalPermissionQueryService);
	}

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		SkoopSecurityExpressionRoot root = new SkoopSecurityExpressionRoot(authentication, userPermissionQueryService,
				userGlobalPermissionQueryService);
		root.setThis(invocation.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(getTrustResolver());
		root.setRoleHierarchy(getRoleHierarchy());
		root.setDefaultRolePrefix(getDefaultRolePrefix());
		return root;
	}
}
