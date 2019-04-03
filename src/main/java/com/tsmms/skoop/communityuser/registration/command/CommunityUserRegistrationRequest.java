package com.tsmms.skoop.communityuser.registration.command;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "CommunityUserRegistrationRequest",
		description = "Request object to create requests to make users members of a community."
)
public class CommunityUserRegistrationRequest {

	private List<String> userIds;

}
