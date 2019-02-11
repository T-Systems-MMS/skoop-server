package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
	private final UserPermissionQueryService userPermissionQueryService;
	private final CommunityQueryService communityQueryService;

	public MethodSecurityConfiguration(UserPermissionQueryService userPermissionQueryService,
									   CommunityQueryService communityQueryService) {
		this.userPermissionQueryService = userPermissionQueryService;
		this.communityQueryService = communityQueryService;
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new MySkillsMethodSecurityExpressionHandler(userPermissionQueryService, communityQueryService);
	}
}
