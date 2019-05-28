package com.tsmms.skoop.userproject;

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
		value = "UserProjectNeedsApprovalNotificationResponse",
		description = "This holds notification data about necessity for" +
				" a manager to approve project membership of her / his subordinate. " +
				"It is used to transfer notification data about necessity for a manager" +
				" to approve project membership of her / his subordinate to a client."
)
@JsonTypeName("UserProjectNeedsApprovalNotification")
public class UserProjectNeedsApprovalNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("The project membership.")
	private UserProjectResponse userProject;

	@Builder
	public UserProjectNeedsApprovalNotificationResponse(String id, LocalDateTime creationDatetime, UserProjectResponse userProject) {
		super(id, creationDatetime);
		this.userProject = userProject;
	}

	public static UserProjectNeedsApprovalNotificationResponse of(UserProjectNeedsApprovalNotification notification) {
		return UserProjectNeedsApprovalNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.userProject(UserProjectResponse.of(notification.getUserProject()))
				.build();
	}

}
