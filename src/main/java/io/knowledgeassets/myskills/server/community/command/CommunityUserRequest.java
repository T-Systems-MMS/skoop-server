package io.knowledgeassets.myskills.server.community.command;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "CommunityUserRequest",
		description = "Request object to make user a member of a community."
)
public class CommunityUserRequest {

	private String userId;

}
