package com.tsmms.skoop.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "GlobalPermissionResponse",
		description = "Permission granted by the user to all users in order to allow them access a specific scope " +
				"of the owner's user data."
)
public class GlobalPermissionResponse {

	@ApiModelProperty("ID of global permission.")
	private String id;

	@ApiModelProperty("Scope of access allowed to the all users.")
	private UserPermissionScope scope;

	@ApiModelProperty("Owning user who grants the permission.")
	private UserSimpleResponse owner;

	public static GlobalPermissionResponse of(GlobalPermission globalPermission) {
		return GlobalPermissionResponse.builder()
				.id(globalPermission.getId())
				.scope(globalPermission.getScope())
				.owner(UserSimpleResponse.of(globalPermission.getOwner()))
				.build();
	}

}
