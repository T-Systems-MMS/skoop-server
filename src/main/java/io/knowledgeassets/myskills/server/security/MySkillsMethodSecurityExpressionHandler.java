package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class MySkillsMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private final UserPermissionQueryService userPermissionQueryService;
	private final SecurityService securityService;

	public MySkillsMethodSecurityExpressionHandler(UserPermissionQueryService userPermissionQueryService,
												   SecurityService securityService) {
		this.userPermissionQueryService = userPermissionQueryService;
		this.securityService = securityService;
	}

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		MySkillsSecurityExpressionRoot root = new MySkillsSecurityExpressionRoot(authentication, userPermissionQueryService, securityService);
		root.setThis(invocation.getThis());
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(getTrustResolver());
		root.setRoleHierarchy(getRoleHierarchy());
		root.setDefaultRolePrefix(getDefaultRolePrefix());
		return root;
	}
}
