package io.knowledgeassets.myskills.server.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserSimpleResponse",
		description = "This holds information of a user. It will be used for sending user information to client."
)
public class UserSimpleResponse {

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

	public static UserSimpleResponse of(User user) {
		return UserSimpleResponse.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.coach(user.getCoach())
				.build();
	}

}
