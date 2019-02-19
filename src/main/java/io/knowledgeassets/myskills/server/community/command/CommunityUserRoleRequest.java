package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "CommunityUserRoleRequest",
		description = "Request object to assign role to a user."
)
public class CommunityUserRoleRequest {

	@NotNull
	private CommunityRole role;

}
