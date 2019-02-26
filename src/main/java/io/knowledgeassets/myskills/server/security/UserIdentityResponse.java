package io.knowledgeassets.myskills.server.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserIdentityResponse",
		description = "This holds the identity details of the authenticated user."
)
public class UserIdentityResponse {
	@ApiModelProperty("User id")
	private String userId;

	@ApiModelProperty("UserName of the authenticated user. It can not be null.")
	private String userName;

	@ApiModelProperty("First name of the authenticated user.")
	private String firstName;

	@ApiModelProperty("Last name of the authenticated user.")
	private String lastName;

	@ApiModelProperty("Email of the authenticated user.")
	private String email;
	@ApiModelProperty("Amount of notifications")
	private Integer notificationCount;
}
