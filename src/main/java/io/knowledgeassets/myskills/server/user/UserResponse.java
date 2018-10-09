package io.knowledgeassets.myskills.server.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserResponse",
		description = "This holds information of a user. It will be used for sending user information to client."
)
public class UserResponse {
	@ApiModelProperty("User id")
	private String id;

	@ApiModelProperty("UserName of the user. It can not be null.")
	private String userName;

	@ApiModelProperty("First name of the user.")
	private String firstName;

	@ApiModelProperty("Last name of the user.")
	private String lastName;

	@ApiModelProperty("Email of the user.")
	private String email;

	@ApiModelProperty("Show as coach?")
	private Boolean coach;
}
