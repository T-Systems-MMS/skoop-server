package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserPermissionCommandService {
	private UserRepository userRepository;
	private UserPermissionRepository userPermissionRepository;

	public UserPermissionCommandService(UserRepository userRepository, UserPermissionRepository userPermissionRepository) {
		this.userRepository = userRepository;
		this.userPermissionRepository = userPermissionRepository;
	}

	@Transactional
	public Stream<UserPermission> replaceOutboundUserPermissions(ReplaceUserPermissionListCommand command) {
		// Create mapping for each scope to the authorized users (merges potential duplicate scope entries).
		final EnumMap<UserPermissionScope, Set<String>> scopeAuthorizedUsers = new EnumMap<>(UserPermissionScope.class);
		command.getUserPermissions().forEach(userPermissionEntry -> {
			if (CollectionUtils.isEmpty(userPermissionEntry.getAuthorizedUserIds())) {
				scopeAuthorizedUsers.put(userPermissionEntry.getScope(), Collections.emptySet());
			} else {
				Set<String> authorizedUsers = scopeAuthorizedUsers.computeIfAbsent(
						userPermissionEntry.getScope(), scope -> new HashSet<>());
				if (userPermissionEntry.getAuthorizedUserIds().stream().anyMatch(userId -> command.getOwnerId().equals(userId))) {
					throw new IllegalArgumentException("Permissions must not be granted to the owner.");
				}
				authorizedUsers.addAll(userPermissionEntry.getAuthorizedUserIds());
			}
		});

		// Determine the owner of the user permission.
		User owner = userRepository.findById(command.getOwnerId()).orElseThrow(() -> NoSuchResourceException.builder()
				.model(Model.USER)
				.searchParamsMap(new String[]{"id", command.getOwnerId()})
				.build());

		// Create new user permissions for the given scopes and authorized users.
		final List<UserPermission> userPermissions = new LinkedList<>();
		scopeAuthorizedUsers.forEach((scope, authorizedUserIds) -> {
			// Remove all existing user permissions for the owner.
			userPermissionRepository.deleteByOwnerIdAndScope(owner.getId(), scope);
			final List<User> authorizedUsers = new LinkedList<>();
			if (CollectionUtils.isNotEmpty(authorizedUserIds)) {
				userRepository.findAllById(authorizedUserIds).forEach(authorizedUsers::add);
			}
			final UserPermission userPermission = UserPermission.builder()
					.id(UUID.randomUUID().toString())
					.owner(owner)
					.scope(scope)
					.authorizedUsers(authorizedUsers)
					.build();
			userPermissions.add(userPermission);
		});
		if (userPermissions.isEmpty()) {
			return Stream.empty();
		} else {
			return StreamSupport.stream(userPermissionRepository.saveAll(userPermissions).spliterator(), false);
		}
	}
}
