package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.user.GlobalPermission;
import com.tsmms.skoop.user.GlobalPermissionRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

@Service
public class UserGlobalPermissionCommandService {

	private final GlobalPermissionRepository globalPermissionRepository;
	private final UserQueryService userQueryService;

	public UserGlobalPermissionCommandService(GlobalPermissionRepository globalPermissionRepository,
											  UserQueryService userQueryService) {
		this.globalPermissionRepository = requireNonNull(globalPermissionRepository);
		this.userQueryService = requireNonNull(userQueryService);
	}

	@Transactional
	public Stream<GlobalPermission> replaceUserGlobalPermissions(ReplaceUserGlobalPermissionListCommand command) {
		if (command == null) {
			throw new IllegalArgumentException("Command to replace user global permissions cannot be null.");
		}
		// Determine the owner of the user permission.
		final User owner = userQueryService.getUserById(command.getOwnerId()).orElseThrow(() -> NoSuchResourceException.builder()
				.model(Model.USER)
				.searchParamsMap(new String[]{"id", command.getOwnerId()})
				.build());
		globalPermissionRepository.deleteByOwnerId(command.getOwnerId());
		final Set<GlobalPermission> globalPermissions = command.getGlobalPermissions().stream().map(globalPermission -> GlobalPermission.builder()
				.id(UUID.randomUUID().toString())
				.owner(owner)
				.scope(globalPermission.getScope())
				.build()
		).collect(Collectors.toSet());
		return StreamSupport.stream(globalPermissionRepository.saveAll(globalPermissions).spliterator(), false);
	}

}
