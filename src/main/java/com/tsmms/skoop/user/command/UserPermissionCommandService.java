package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

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
			Set<String> authorizedUsers = scopeAuthorizedUsers.computeIfAbsent(
					userPermissionEntry.getScope(), scope -> new HashSet<>());
			authorizedUsers.addAll(userPermissionEntry.getAuthorizedUserIds());
		});

		// Determine the owner of the user permission.
		User owner = userRepository.findById(command.getOwnerId()).orElseThrow(() -> NoSuchResourceException.builder()
				.model(Model.USER)
				.searchParamsMap(new String[]{"id", command.getOwnerId()})
				.build());

		// Remove all existing user permissions for the owner.
		userPermissionRepository.deleteByOwnerId(command.getOwnerId());

		// Create new user permissions for the given scopes and authorized users.
		List<UserPermission> userPermissions = new ArrayList<>();
		scopeAuthorizedUsers.forEach((scope, authorizedUserIds) -> {
			List<User> authorizedUsers = new ArrayList<>();
			userRepository.findAllById(authorizedUserIds).forEach(authorizedUsers::add);
			UserPermission userPermission = UserPermission.builder()
					.id(UUID.randomUUID().toString())
					.owner(owner)
					.scope(scope)
					.authorizedUsers(authorizedUsers)
					.build();
			userPermissions.add(userPermissionRepository.save(userPermission));
		});

		return userPermissions.stream();
	}
}
