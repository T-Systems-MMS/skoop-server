package com.tsmms.skoop.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class SkoopMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private final SecurityService securityService;

	public SkoopMethodSecurityExpressionHandler(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		SkoopSecurityExpressionRoot root = new SkoopSecurityExpressionRoot(authentication, securityService);
		root.setThis(invocation.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(getTrustResolver());
		root.setRoleHierarchy(getRoleHierarchy());
		root.setDefaultRolePrefix(getDefaultRolePrefix());
		return root;
	}
}
