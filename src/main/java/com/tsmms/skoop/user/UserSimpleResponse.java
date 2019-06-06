package com.tsmms.skoop.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
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

	@Builder(builderMethodName = "userSimpleResponseBuilder")
	public UserSimpleResponse(String id, String userName, String firstName, String lastName, String email) {
		this.id = id;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public static UserSimpleResponse of(User user) {
		if (user == null) {
			return null;
		}
		return UserSimpleResponse.userSimpleResponseBuilder()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.build();
	}

	public static List<UserSimpleResponse> convertUserListToUserSimpleResponseList(List<User> users) {
		if (users == null) {
			return Collections.emptyList();
		}
		return users.stream().map(UserSimpleResponse::of).collect(toList());
	}

}
