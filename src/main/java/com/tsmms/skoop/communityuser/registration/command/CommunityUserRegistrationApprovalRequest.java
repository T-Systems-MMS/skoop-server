package com.tsmms.skoop.communityuser.registration.command;

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
		value = "CommunityUserRegistrationApproval",
		description = "Request object to approve user registration."
)
public class CommunityUserRegistrationApprovalRequest {

	private Boolean approvedByUser;
	private Boolean approvedByCommunity;

	public CommunityUserRegistrationApprovalCommand command() {
		return CommunityUserRegistrationApprovalCommand.builder()
				.approvedByCommunity(approvedByCommunity)
				.approvedByUser(approvedByUser)
				.build();
	}

}
