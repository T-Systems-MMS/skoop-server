package com.tsmms.skoop.security;

import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

/**
 * Service to accumulate security logic used by {@link SkoopSecurityExpressionRoot}
 * to make it available withing the whole application.
 */
@Service
public class SecurityService {

	private final CommunityQueryService communityQueryService;
	private final CurrentUserService currentUserService;
	private final UserPermissionQueryService userPermissionQueryService;

	public SecurityService(CommunityQueryService communityQueryService,
						   CurrentUserService currentUserService,
						   UserPermissionQueryService userPermissionQueryService) {
		this.communityQueryService = requireNonNull(communityQueryService);
		this.currentUserService = requireNonNull(currentUserService);
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
	}

	/**
	 * Checks whether user has community manager role for the community referenced by the community ID.
	 *
	 * @param communityId ID of the community
	 * @param userId ID of the user
	 * @return <code>true</code> if the user has community manager role for the community referenced by the community ID.
	 */
	public boolean isCommunityManager(String userId, String communityId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("User identifier is not defined.");
		}
		if (StringUtils.isEmpty(communityId)) {
			throw new IllegalArgumentException("Community identifier is not defined.");
		}
		return communityQueryService.hasCommunityManagerRole(userId, communityId);
	}

	/**
	 * Checks whether the authenticated user has community manager role for the community referenced by the community ID.
	 *
	 * @param communityId ID of the community
	 * @return <code>true</code> if the authenticated user has community manager role for the community referenced by the community ID.
	 */
	public boolean isCommunityManager(String communityId) {
		return isCommunityManager(currentUserService.getCurrentUserId(), communityId);
	}

	/**
	 * Checks whether user is a member of the community referenced by the community ID.
	 *
	 * @param communityId ID of the community
	 * @param userId ID of the user
	 * @return <code>true</code> if the user is a member of the community referenced by the community ID.
	 */
	public boolean isCommunityMember(String userId, String communityId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("User identifier is not defined.");
		}
		if (StringUtils.isEmpty(communityId)) {
			throw new IllegalArgumentException("Community identifier is not defined.");
		}
		return communityQueryService.isCommunityMember(userId, communityId);
	}

	/**
	 * Checks whether the authenticated user is a member of the community referenced by the community ID.
	 *
	 * @param communityId ID of the community
	 * @return <code>true</code> if the authenticated user is a member of the community referenced by the community ID.
	 */
	public boolean isCommunityMember(String communityId) {
		return isCommunityMember(currentUserService.getCurrentUserId(), communityId);
	}


	/**
	 * Checks whether the user referenced by the owner ID has granted the user permission with the given scope to the
	 * user with the specified ID.
	 *
	 * @param userId ID of the user who is checked to have a permission
	 * @param ownerId ID of the user who owns the protected resource.
	 * @param scope   Scope of the user permission to check. See {@link UserPermissionScope}
	 * @return <code>true</code> if the user referenced by owner ID has granted the user permission with the given scope
	 * to the specified user.
	 */
	public boolean hasUserPermission(String ownerId, String userId, UserPermissionScope scope) {
		return userPermissionQueryService.hasUserPermission(ownerId, userId, scope);
	}

	/**
	 * Checks whether the given user ID equals the user ID of the authenticated user.
	 *
	 * @param userId User ID to check against the authenticated user.
	 * @return <code>true</code> if the given user ID equals the user ID of the authenticated user.
	 */
	public boolean isAuthenticatedUserId(String userId) {
		return isAuthenticatedUserId(currentUserService.getCurrentUserId(), userId);
	}
	/**
	 * Checks whether the given user ID equals the user ID of the authenticated user.
	 *
	 * @param authenticatedUserId User ID of the authenticated user.
	 * @param userId User ID to check against the authenticated user.
	 * @return <code>true</code> if the given user ID equals the user ID of the authenticated user.
	 */
	public boolean isAuthenticatedUserId(String authenticatedUserId, String userId) {
		return userId == null && authenticatedUserId == null || userId != null && userId.equals(authenticatedUserId);
	}


}
