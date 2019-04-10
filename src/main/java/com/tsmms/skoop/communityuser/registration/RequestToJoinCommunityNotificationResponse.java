package com.tsmms.skoop.communityuser.registration;

import com.fasterxml.jackson.annotation.JsonTypeName;
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
		value = "RequestToJoinCommunityNotificationResponse",
		description = "This holds notification data about user registration when the user sent request to join a community. " +
				" It is used to transfer user registration notification data to a client."
)
@JsonTypeName("RequestToJoinCommunityNotification")
public class RequestToJoinCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Registration the notification is associated with.")
	private CommunityUserRegistrationResponse registration;

	@ApiModelProperty("Community name. It should be used by a client in case there is no reference to the community.")
	private String communityName;

	@Builder
	public RequestToJoinCommunityNotificationResponse(String id,
													  LocalDateTime creationDatetime,
													  CommunityUserRegistrationResponse registration,
													  String communityName) {
		super(id, creationDatetime);
		this.registration = registration;
		this.communityName = communityName;
	}

	public static RequestToJoinCommunityNotificationResponse of(RequestToJoinCommunityNotification notification) {
		return RequestToJoinCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.registration(CommunityUserRegistrationResponse.of(notification.getRegistration()))
				.communityName(notification.getCommunityName())
				.build();
	}

}
