package com.tsmms.skoop.communityuser;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tsmms.skoop.community.CommunityResponse;
import com.tsmms.skoop.user.UserSimpleResponse;
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
		value = "UserLeftCommunityNotificationResponse",
		description = "This holds notification data about user left community event. " +
				"It is used to transfer user left community notification data to a client."
)
@JsonTypeName("UserLeftCommunityNotification")
public class UserLeftCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("The community user kicked from.")
	private CommunityResponse community;

	@ApiModelProperty("User kicked out from a community.")
	private UserSimpleResponse user;

	@Builder
	public UserLeftCommunityNotificationResponse(String id, LocalDateTime creationDatetime, CommunityResponse community, UserSimpleResponse user) {
		super(id, creationDatetime);
		this.community = community;
		this.user = user;
	}

	public static UserLeftCommunityNotificationResponse of(UserLeftCommunityNotification notification) {
		return UserLeftCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.community(CommunityResponse.of(notification.getCommunity()))
				.user(UserSimpleResponse.of(notification.getUser()))
				.build();
	}

}
