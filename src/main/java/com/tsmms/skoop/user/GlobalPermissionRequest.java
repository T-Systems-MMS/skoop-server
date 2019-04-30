package com.tsmms.skoop.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "GlobalPermissionRequest",
		description = "Request object for creating or updating user global permission."
)
public class GlobalPermissionRequest {

	@ApiModelProperty("Scope of access to be granted to all users.")
	@NotBlank
	private UserPermissionScope scope;

}
