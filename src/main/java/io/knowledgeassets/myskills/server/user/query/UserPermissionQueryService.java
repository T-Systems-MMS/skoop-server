package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserPermission;
import io.knowledgeassets.myskills.server.user.UserPermissionRepository;
import io.knowledgeassets.myskills.server.user.UserPermissionScope;
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
	public Stream<UserPermission> getUserPermissionsByOwnerId(String ownerId) {
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
}
