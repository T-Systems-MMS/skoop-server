package io.knowledgeassets.myskills.server.notification;

import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "NotificationResponse",
		description = "This holds notification data. It is used to transfer notification data to a client."
)
public class NotificationResponse {

	@ApiModelProperty("Identifier of the notification.")
	private String id;

	@ApiModelProperty("Datetime when notification was created")
	private LocalDateTime creationDatetime;

	@ApiModelProperty("Type of the notification.")
	private NotificationType notificationType;

	@ApiModelProperty("Registration the notification is associated with.")
	private CommunityUserRegistrationResponse registration;

	@ApiModelProperty("Additional attributes of the notification.")
	private Map<String, Object> attributes;

	public static NotificationResponse of(Notification notification) {
		return NotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.notificationType(notification.getType())
				.registration(CommunityUserRegistrationResponse.of(notification.getRegistration()))
				.attributes(new HashMap<>())
				.build();
	}

}
