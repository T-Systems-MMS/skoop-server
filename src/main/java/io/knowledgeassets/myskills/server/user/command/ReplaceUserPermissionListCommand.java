package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.user.UserPermissionScope;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReplaceUserPermissionListCommand {
	private String ownerId;
	private List<UserPermissionEntry> userPermissions;

	@Data
	@Builder
	public static class UserPermissionEntry {
		private UserPermissionScope scope;
		private List<String> authorizedUserIds;
	}
}
