package io.knowledgeassets.myskills.server.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(
		value = "UserIdentityResponse",
		description = "This holds the identity details of the authenticated user. It will be used for sending user " +
				"details to client."
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

	@ApiModelProperty("Authorities of the authenticated user.")
	private List<String> roles;
}
