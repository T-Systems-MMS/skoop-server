package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.user.UserSimpleResponse;
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
