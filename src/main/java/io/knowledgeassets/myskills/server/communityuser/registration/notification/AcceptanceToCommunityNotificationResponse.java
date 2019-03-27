package io.knowledgeassets.myskills.server.communityuser.registration.notification;

import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationResponse;
import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import io.knowledgeassets.myskills.server.notification.Notification;
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
		value = "AcceptanceToCommunityNotificationResponse",
		description = "This holds notification data about user registration when a user was let to join a community." +
				" It is used to transfer user registration notification data to a client."
)
public class AcceptanceToCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Registration the notification is associated with.")
	private CommunityUserRegistrationResponse registration;

	@Builder
	public AcceptanceToCommunityNotificationResponse(String id,
													 LocalDateTime creationDatetime,
													 CommunityUserRegistrationResponse registration) {
		super(id, creationDatetime);
		this.registration = registration;
	}

	public static AcceptanceToCommunityNotificationResponse of(Notification notification) {
		return AcceptanceToCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.registration(CommunityUserRegistrationResponse.of(notification.getRegistration()))
				.build();
	}

}