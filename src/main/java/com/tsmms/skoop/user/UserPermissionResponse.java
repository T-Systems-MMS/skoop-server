package com.tsmms.skoop.user;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
@ApiModel(
		value = "UserPermissionResponse",
		description = "Permission granted by the user to other users in order to allow them access a specific scope " +
				"of the owner's user data."
)
@JsonTypeName("UserPermissionResponse")
public class UserPermissionResponse extends PermissionResponse {

	@ApiModelProperty("Scope of access allowed to the authorized users")
	private UserPermissionScope scope;

	@ApiModelProperty("Users who are granted access to the scope of user data")
	private List<UserSimpleResponse> authorizedUsers;

	@Builder
	public UserPermissionResponse(String id, UserSimpleResponse owner, UserPermissionScope scope, List<UserSimpleResponse> authorizedUsers) {
		super(id, owner);
		this.scope = scope;
		this.authorizedUsers = authorizedUsers;
	}

	public static UserPermissionResponse of(UserPermission userPermission) {
		return UserPermissionResponse.builder()
				.owner(UserSimpleResponse.of(userPermission.getOwner()))
				.scope(userPermission.getScope())
				.authorizedUsers(userPermission.getAuthorizedUsers() != null ?
						userPermission.getAuthorizedUsers().stream()
								.map(UserSimpleResponse::of)
								.collect(toList()) :
						emptyList())
				.build();
	}
}
