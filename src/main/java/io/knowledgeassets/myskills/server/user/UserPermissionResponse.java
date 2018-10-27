package io.knowledgeassets.myskills.server.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@ApiModel(
		value = "UserPermissionResponse",
		description = "Permission granted by the user to other users in order to allow them access a specific scope " +
				"of the owner's user data."
)
public class UserPermissionResponse {
	@ApiModelProperty("Owning user who grants the permission")
	private UserResponse owner;

	@ApiModelProperty("Scope of access allowed to the authorized users")
	private UserPermissionScope scope;

	@ApiModelProperty("Users who are granted access to the scope of user data")
	private List<UserResponse> authorizedUsers;

	public static UserPermissionResponse of(UserPermission userPermission) {
		return UserPermissionResponse.builder()
				.owner(UserResponse.of(userPermission.getOwner()))
				.scope(userPermission.getScope())
				.authorizedUsers(userPermission.getAuthorizedUsers().stream()
						.map(UserResponse::of)
						.collect(toList()))
				.build();
	}
}
