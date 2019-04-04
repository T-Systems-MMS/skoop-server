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

	@Builder
	public RequestToJoinCommunityNotificationResponse(String id,
													  LocalDateTime creationDatetime,
													  CommunityUserRegistrationResponse registration) {
		super(id, creationDatetime);
		this.registration = registration;
	}

	public static RequestToJoinCommunityNotificationResponse of(RequestToJoinCommunityNotification notification) {
		return RequestToJoinCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.registration(CommunityUserRegistrationResponse.of(notification.getRegistration()))
				.build();
	}

}
