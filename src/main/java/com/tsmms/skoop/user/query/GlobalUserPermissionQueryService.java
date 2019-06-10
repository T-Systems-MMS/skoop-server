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
	public Stream<GlobalUserPermission> getOutboundGlobalUserPermissions(String ownerId) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return globalUserPermissionRepository.findByOwnerId(ownerId);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getOutboundGlobalUserPermissionsByScope(String ownerId, GlobalUserPermissionScope scope) {
		if (scope == null) {
			throw new IllegalArgumentException("Global user permission scope cannot be null.");
		}
 		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return globalUserPermissionRepository.findByOwnerIdAndScope(ownerId, scope);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getInboundGlobalUserPermissions(String ownerId) {
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return stream(globalUserPermissionRepository.getInboundGlobalUserPermissions(ownerId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<GlobalUserPermission> getInboundGlobalUserPermissionsByScope(String ownerId, GlobalUserPermissionScope scope) {
		if (scope == null) {
			throw new IllegalArgumentException("Global user permission scope cannot be null.");
		}
		if (ownerId == null) {
			throw new IllegalArgumentException("Owner ID cannot be null.");
		}
		return stream(globalUserPermissionRepository.getInboundGlobalUserPermissionsByScope(ownerId, scope).spliterator(), false);
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

}
