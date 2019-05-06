package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

@Service
public class GlobalUserPermissionCommandService {

	private final GlobalUserPermissionRepository globalUserPermissionRepository;
	private final UserQueryService userQueryService;

	public GlobalUserPermissionCommandService(GlobalUserPermissionRepository globalUserPermissionRepository,
											  UserQueryService userQueryService) {
		this.globalUserPermissionRepository = requireNonNull(globalUserPermissionRepository);
		this.userQueryService = requireNonNull(userQueryService);
	}

	@Transactional
	public Stream<GlobalUserPermission> replaceGlobalUserPermissions(ReplaceGlobalUserPermissionListCommand command) {
		if (command == null) {
			throw new IllegalArgumentException("Command to replace user global permissions cannot be null.");
		}
		if (!userQueryService.exists(command.getOwnerId())) {
			throw NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(new String[]{"id", command.getOwnerId()})
					.build();
		}
		globalUserPermissionRepository.deleteByOwnerId(command.getOwnerId());
		// Determine the owner of the user permission.
		final User owner = userQueryService.getUserById(command.getOwnerId()).orElseThrow(() -> NoSuchResourceException.builder()
				.model(Model.USER)
				.searchParamsMap(new String[]{"id", command.getOwnerId()})
				.build());
		final Set<GlobalUserPermission> globalUserPermissions = command.getGlobalPermissions().stream().map(globalPermission -> GlobalUserPermission.builder()
				.id(UUID.randomUUID().toString())
				.owner(owner)
				.scope(globalPermission.getScope())
				.build()
		).collect(toSet());
		return StreamSupport.stream(globalUserPermissionRepository.saveAll(globalUserPermissions).spliterator(), false);
	}

}
