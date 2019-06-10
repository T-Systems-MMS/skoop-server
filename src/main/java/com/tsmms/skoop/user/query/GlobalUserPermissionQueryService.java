package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionRepository;
import com.tsmms.skoop.user.GlobalUserPermissionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

@Service
public class GlobalUserPermissionQueryService {

	private final GlobalUserPermissionRepository globalUserPermissionRepository;

	public GlobalUserPermissionQueryService(GlobalUserPermissionRepository globalUserPermissionRepository) {
		this.globalUserPermissionRepository = requireNonNull(globalUserPermissionRepository);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getGlobalUserPermissions(String ownerId) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return globalUserPermissionRepository.findByOwnerId(ownerId);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getGlobalUserPermissionsGrantedToUser(String userId) {
		return stream(globalUserPermissionRepository.getGlobalUserPermissionsGrantedToUser(userId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public boolean isGlobalUserPermissionGranted(String ownerId, GlobalUserPermissionScope scope) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		if (scope == null) {
			throw new IllegalArgumentException("Scope cannot be null.");
		}
		return globalUserPermissionRepository.isGlobalPermissionGranted(ownerId, scope);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getGlobalUserPermissionsByScope(String userId, GlobalUserPermissionScope scope) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		if (scope == null) {
			throw new IllegalArgumentException("Scope cannot be null.");
		}
		return stream(globalUserPermissionRepository.getGlobalUserPermissionsByScopeGrantedToUser(userId, scope).spliterator(), false);
	}

}
