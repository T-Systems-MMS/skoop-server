package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.user.UserPermission;
import io.knowledgeassets.myskills.server.user.UserPermissionRepository;
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
}
