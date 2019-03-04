package io.knowledgeassets.myskills.server.communityuser.registration.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityUserRegistrationApprovalCommand {

	private Boolean approvedByUser;
	private Boolean approvedByCommunity;

}
