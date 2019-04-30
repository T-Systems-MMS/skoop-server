package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.UserPermissionScope;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ReplaceUserGlobalPermissionListCommand {

	private String ownerId;
	private Set<UserGlobalPermissionEntry> globalPermissions;

	@Data
	@Builder
	public static class UserGlobalPermissionEntry {
		private UserPermissionScope scope;
	}

}
