package com.tsmms.skoop.user.query;

import com.google.common.base.Enums;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionScope;
import com.tsmms.skoop.user.Permission;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.concat;

@Service
public class PermissionQueryService {

	private final UserPermissionQueryService userPermissionQueryService;
	private final GlobalUserPermissionQueryService globalUserPermissionQueryService;

	public PermissionQueryService(UserPermissionQueryService userPermissionQueryService,
								  GlobalUserPermissionQueryService globalUserPermissionQueryService) {
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
		this.globalUserPermissionQueryService = requireNonNull(globalUserPermissionQueryService);
	}

	@Transactional(readOnly = true)
	public Stream<Permission> getIncomingPermissionsByScope(String userId, String scope) {
		return concat(getUserPermissionStream(userId, scope), getGlobalUserPermissionStream(userId, scope));
	}

	private Stream<UserPermission> getUserPermissionStream(String userId, String scope) {
		final Stream<UserPermission> userPermissions;
		final Optional<UserPermissionScope> userPermissionScope = Enums.getIfPresent(UserPermissionScope.class, scope)
				.transform(Optional::of).or(Optional.empty());
		if (userPermissionScope.isPresent()) {
			userPermissions = userPermissionQueryService.getInboundUserPermissionsByAuthorizedUserIdAndScope(userId, userPermissionScope.get());
		} else {
			userPermissions = userPermissionQueryService.getInboundUserPermissionsByAuthorizedUserId(userId);
		}
		return userPermissions;
	}

	private Stream<GlobalUserPermission> getGlobalUserPermissionStream(String userId, String scope) {
		final Stream<GlobalUserPermission> globalUserPermissionStream;
		final Optional<GlobalUserPermissionScope> globalUserPermissionScope = Enums.getIfPresent(GlobalUserPermissionScope.class, scope)
				.transform(Optional::of).or(Optional.empty());
		if (globalUserPermissionScope.isPresent()) {
			globalUserPermissionStream = globalUserPermissionQueryService.getGlobalUserPermissionsByScope(userId, globalUserPermissionScope.get());
		} else {
			globalUserPermissionStream = globalUserPermissionQueryService.getGlobalUserPermissionsGrantedToUser(userId);
		}
		return globalUserPermissionStream;
	}

}
