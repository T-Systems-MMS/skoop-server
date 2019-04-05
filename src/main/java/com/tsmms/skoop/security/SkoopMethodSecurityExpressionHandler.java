package com.tsmms.skoop.security;

import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class SkoopMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private final UserPermissionQueryService userPermissionQueryService;

	public SkoopMethodSecurityExpressionHandler(UserPermissionQueryService userPermissionQueryService) {
		this.userPermissionQueryService = userPermissionQueryService;
	}

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		SkoopSecurityExpressionRoot root = new SkoopSecurityExpressionRoot(authentication, userPermissionQueryService);
		root.setThis(invocation.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(getTrustResolver());
		root.setRoleHierarchy(getRoleHierarchy());
		root.setDefaultRolePrefix(getDefaultRolePrefix());
		return root;
	}
}
