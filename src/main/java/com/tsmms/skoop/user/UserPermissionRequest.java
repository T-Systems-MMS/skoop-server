package com.tsmms.skoop.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "UserPermissionRequest",
		description = "Request object for creating or updating a user permission."
)
public class UserPermissionRequest {
	@ApiModelProperty("Scope of access to be granted to the authorized users.")
	@NotBlank
	private UserPermissionScope scope;

	@ApiModelProperty("IDs of the users to grant the permission to.")
	private List<String> authorizedUserIds;
}
