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
		value = "InvitationToJoinCommunityNotificationResponse",
		description = "This holds notification data about user registration when the user was invited to join a community. " +
				"It is used to transfer user registration notification data to a client."
)
@JsonTypeName("InvitationToJoinCommunityNotification")
public class InvitationToJoinCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Registration the notification is associated with.")
	private CommunityUserRegistrationResponse registration;

	@Builder
	public InvitationToJoinCommunityNotificationResponse(String id,
														 LocalDateTime creationDatetime,
														 CommunityUserRegistrationResponse registration) {
		super(id, creationDatetime);
		this.registration = registration;
	}

	public static InvitationToJoinCommunityNotificationResponse of(InvitationToJoinCommunityNotification notification) {
		return InvitationToJoinCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.registration(CommunityUserRegistrationResponse.of(notification.getRegistration()))
				.build();
	}

}
