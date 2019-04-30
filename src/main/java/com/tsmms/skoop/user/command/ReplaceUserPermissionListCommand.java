package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.UserPermissionScope;
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
