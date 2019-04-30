package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.GlobalPermission;
import com.tsmms.skoop.user.GlobalPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class UserGlobalPermissionQueryService {

	private final GlobalPermissionRepository globalPermissionRepository;

	public UserGlobalPermissionQueryService(GlobalPermissionRepository globalPermissionRepository) {
		this.globalPermissionRepository = requireNonNull(globalPermissionRepository);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalPermission> getUserGlobalPermissions(String ownerId) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return globalPermissionRepository.findByOwnerId(ownerId);
	}

	@Transactional(readOnly = true)
	public boolean isGlobalPermissionGranted(String ownerId, UserPermissionScope scope) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		if (scope == null) {
			throw new IllegalArgumentException("Scope cannot be null.");
		}
		return globalPermissionRepository.isGlobalPermissionGranted(ownerId, scope);
	}

}
