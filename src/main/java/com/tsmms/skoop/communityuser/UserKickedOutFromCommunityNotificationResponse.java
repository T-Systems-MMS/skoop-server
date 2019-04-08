package com.tsmms.skoop.communityuser;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tsmms.skoop.community.CommunityResponse;
import com.tsmms.skoop.notification.AbstractNotificationResponse;
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
		value = "UserKickedOutFromCommunityNotificationResponse",
		description = "This holds notification data about user kicked out event. " +
				"It is used to transfer user kicked out notification data to a client."
)
@JsonTypeName("UserKickedOutFromCommunityNotification")
public class UserKickedOutFromCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("The community user kicked from.")
	private CommunityResponse community;

	@ApiModelProperty("Community name. It should be used by a client in case there is no reference to the community.")
	private String communityName;

	@Builder
	public UserKickedOutFromCommunityNotificationResponse(String id, LocalDateTime creationDatetime, CommunityResponse community,
														  String communityName) {
		super(id, creationDatetime);
		this.community = community;
		this.communityName = communityName;
	}

	public static UserKickedOutFromCommunityNotificationResponse of(UserKickedOutFromCommunityNotification notification) {
		return UserKickedOutFromCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.community(CommunityResponse.of(notification.getCommunity()))
				.communityName(notification.getCommunityName())
				.build();
	}

}
