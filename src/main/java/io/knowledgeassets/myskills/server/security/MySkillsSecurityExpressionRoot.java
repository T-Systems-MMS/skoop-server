package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.UserPermissionScope;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;

public class MySkillsSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private Object filterObject;
	private Object returnObject;
	private Object target;
	private final UserPermissionQueryService userPermissionQueryService;
	private final SecurityService securityService;

	public MySkillsSecurityExpressionRoot(Authentication authentication, UserPermissionQueryService userPermissionQueryService,
										  SecurityService securityService) {
		super(authentication);
		this.userPermissionQueryService = userPermissionQueryService;
		this.securityService = securityService;
	}

	/**
	 * Checks whether the given user ID equals the user ID of the authenticated principal.
	 * <p>Usage example assuming a method with a parameter named "userId":</p>
	 * <p>@PreAuthorize("isPrincipalUserId(#userId)")</p>
	 *
	 * @param userId User ID to check against the principal.
	 * @return <code>true</code> if the given user ID equals the user ID of the authenticated principal.
	 */
	public boolean isPrincipalUserId(String userId) {
		Object principal = getPrincipal();
		if (principal instanceof Jwt) {
			String userIdClaim = ((Jwt) principal).getClaimAsString(MYSKILLS_USER_ID);
			return userId == null && userIdClaim == null || userId != null && userId.equals(userIdClaim);
		}
		return false;
	}

	/**
	 * Checks whether the user referenced by the owner ID has granted the user permission with the given scope to the
	 * authenticated principal.
	 * <p>Usage example assuming a method with a parameter named "userId":</p>
	 * <p>@PreAuthorize("hasUserPermission(#userId, 'READ_USER_SKILLS')")</p>
	 *
	 * @param ownerId ID of the user who owns the protected resource.
	 * @param scope   Scope of the user permission to check. See {@link UserPermissionScope}
	 * @return <code>true</code> if the user referenced by owner ID has granted the user permission with the given scope
	 * to the authenticated principal.
	 */
	public boolean hasUserPermission(String ownerId, String scope) {
		Object principal = getPrincipal();
		if (principal instanceof Jwt) {
			String userIdClaim = ((Jwt) principal).getClaimAsString(MYSKILLS_USER_ID);
			return userPermissionQueryService.hasUserPermission(ownerId, userIdClaim,
					UserPermissionScope.valueOf(scope));
		}
		return false;
	}

	/**
	 * Checks whether the authenticated user has community manager role for the community referenced by the community ID.
	 * <p>Usage example assuming a method with a parameter named "communityId":</p>
	 * <p>@PreAuthorize("hasCommunityManagerRole(#communityId)")</p>
	 *
	 * @param communityId ID of the community
	 * @return <code>true</code> if the authenticated user has community manager role for the community referenced by the community ID.
	 */
	public boolean hasCommunityManagerRole(String communityId) {
		Object principal = getPrincipal();
		if (principal instanceof Jwt) {
			return securityService.hasCommunityManagerRole((Jwt) principal, communityId);
		}
		return false;
	}

	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	public Object getFilterObject() {
		return filterObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	/**
	 * Sets the "this" property for use in expressions. Typically this will be the "this" property of the {@code
	 * JoinPoint} representing the method invocation which is being protected.
	 *
	 * @param target the target object on which the method in is being invoked.
	 */
	void setThis(Object target) {
		this.target = target;
	}

	public Object getThis() {
		return target;
	}
}
