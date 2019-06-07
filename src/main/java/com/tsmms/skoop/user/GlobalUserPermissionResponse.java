package com.tsmms.skoop.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@ApiModel(
		value = "GlobalPermissionResponse",
		description = "Permission granted by the user to all users in order to allow them access a specific scope " +
				"of the owner's user data."
)
@JsonTypeName("GlobalUserPermissionResponse")
public class GlobalUserPermissionResponse extends PermissionResponse {

	@ApiModelProperty("Scope of access allowed to the all users.")
	private GlobalUserPermissionScope scope;

	@Builder
	public GlobalUserPermissionResponse(String id, UserSimpleResponse owner, GlobalUserPermissionScope scope) {
		super(id, owner);
		this.scope = scope;
	}

	public static GlobalUserPermissionResponse of(GlobalUserPermission globalUserPermission) {
		return GlobalUserPermissionResponse.builder()
				.id(globalUserPermission.getId())
				.scope(globalUserPermission.getScope())
				.owner(UserSimpleResponse.of(globalUserPermission.getOwner()))
				.build();
	}

}
