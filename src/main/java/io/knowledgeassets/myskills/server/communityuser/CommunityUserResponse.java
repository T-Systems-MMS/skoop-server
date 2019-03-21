package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "CommunityUserResponse",
		description = "This holds community user data. It is used to transfer community user data to a client."
)
public class CommunityUserResponse {

	private UserSimpleResponse user;
	private CommunityRole role;

	public static CommunityUserResponse of(CommunityUser communityUser) {
		return CommunityUserResponse.builder()
				.user(UserSimpleResponse.of(communityUser.getUser()))
				.role(communityUser.getRole())
				.build();
	}

}
