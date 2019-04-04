package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserPermissionQueryService {
	private UserPermissionRepository userPermissionRepository;

	public UserPermissionQueryService(UserPermissionRepository userPermissionRepository) {
		this.userPermissionRepository = userPermissionRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getOutboundUserPermissionsByOwnerId(String ownerId) {
		return StreamSupport.stream(userPermissionRepository.findByOwnerId(ownerId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsersWhoGrantedPermission(String authorizedUserId, UserPermissionScope scope) {
		return StreamSupport.stream(userPermissionRepository.findUsersWhoGrantedPermission(authorizedUserId, scope)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public boolean hasUserPermission(String ownerId, String authorizedUserId, UserPermissionScope scope) {
		return userPermissionRepository.hasUserPermission(ownerId, authorizedUserId, scope);
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getInboundUserPermissionsByAuthorizedUserId(String authorizedUserId) {
		return StreamSupport.stream(userPermissionRepository.findByAuthorizedUsersId(authorizedUserId)
				.spliterator(), false);
	}

}
