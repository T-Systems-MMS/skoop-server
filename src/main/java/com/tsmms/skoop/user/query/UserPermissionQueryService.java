package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@Service
public class UserPermissionQueryService {
	private UserPermissionRepository userPermissionRepository;

	public UserPermissionQueryService(UserPermissionRepository userPermissionRepository) {
		this.userPermissionRepository = userPermissionRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getOutboundUserPermissionsByOwnerId(String ownerId) {
		return stream(userPermissionRepository.findByOwnerId(ownerId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getOutboundUserPermissionsByOwnerIdAndScope(String ownerId, UserPermissionScope scope) {
		return userPermissionRepository.findByOwnerIdAndScope(ownerId, scope);
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsersWhoGrantedPermission(String authorizedUserId, UserPermissionScope scope) {
		return stream(userPermissionRepository.findUsersWhoGrantedPermission(authorizedUserId, scope)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public boolean hasUserPermission(String ownerId, String authorizedUserId, UserPermissionScope scope) {
		return userPermissionRepository.hasUserPermission(ownerId, authorizedUserId, scope);
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getInboundUserPermissionsByAuthorizedUserId(String authorizedUserId) {
		return stream(userPermissionRepository.findByAuthorizedUsersId(authorizedUserId)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserPermission> getInboundUserPermissionsByAuthorizedUserIdAndScope(String authorizedUserId, UserPermissionScope scope) {
		return userPermissionRepository.findByAuthorizedUsersIdAndScope(authorizedUserId, scope);
	}

}
