package io.knowledgeassets.myskills.server.user;

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
@ApiModel(value = "UserRequest"
		, description = "Request object for creating or updating a user.")
public class UserRequest {

	@ApiModelProperty("UserName of the user. It can not be null.")
	@NotBlank
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
