package com.tsmms.skoop.user.notification;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tsmms.skoop.notification.AbstractNotificationResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ApiModel(
		value = "UserWelcomeNotificationResponse",
		description = "The notification to welcome user logged in to the application for the first time."
)
@JsonTypeName("UserWelcomeNotification")
public class UserWelcomeNotificationResponse extends AbstractNotificationResponse {

	@Builder
	public UserWelcomeNotificationResponse(String id, LocalDateTime creationDatetime) {
		super(id, creationDatetime);
	}

	public static UserWelcomeNotificationResponse of(UserWelcomeNotification notification) {
		return UserWelcomeNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.build();
	}
}
