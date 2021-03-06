package com.tsmms.skoop.communityuser.command;

import com.tsmms.skoop.community.CommunityRole;
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
		value = "CommunityUserUpdateRequest",
		description = "Request object to assign role to a user."
)
public class CommunityUserUpdateRequest {

	@NotNull
	private CommunityRole role;

}
