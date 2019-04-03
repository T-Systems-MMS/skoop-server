package io.knowledgeassets.myskills.server.communityuser;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "CommunityUserRoleChangedNotificationResponse",
		description = "This holds notification data about community user role changed event. " +
				"It is used to transfer community user role changed notification data to a client."
)
@JsonTypeName("CommunityUserRoleChangedNotification")
public class CommunityUserRoleChangedNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("The new user role in a community.")
	private CommunityRole role;

	@ApiModelProperty("Community name.")
	private String communityName;

	@Builder
	public CommunityUserRoleChangedNotificationResponse(String id, LocalDateTime creationDatetime, CommunityRole role, String communityName) {
		super(id, creationDatetime);
		this.role = role;
		this.communityName = communityName;
	}

	public static CommunityUserRoleChangedNotificationResponse of(CommunityUserRoleChangedNotification notification) {
		return CommunityUserRoleChangedNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.role(notification.getRole())
				.communityName(notification.getCommunityName())
				.build();
	}

}
