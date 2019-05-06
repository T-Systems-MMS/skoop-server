package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.GlobalUserPermissionScope;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ReplaceGlobalUserPermissionListCommand {

	private String ownerId;
	private Set<GlobalUserPermissionEntry> globalPermissions;

	@Data
	@Builder
	public static class GlobalUserPermissionEntry {
		private GlobalUserPermissionScope scope;
	}

}
