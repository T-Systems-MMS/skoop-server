package com.tsmms.skoop.community;

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
		value = "CommunityDeletedNotificationResponse",
		description = "This holds notification data about community deletion. " +
				"It is used to transfer community deleted notification data to a client."
)
@JsonTypeName("CommunityDeletedNotification")
public class CommunityDeletedNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Name of deleted community.")
	private String communityName;

	@Builder
	public CommunityDeletedNotificationResponse(String id, LocalDateTime creationDatetime, String communityName) {
		super(id, creationDatetime);
		this.communityName = communityName;
	}

	public static CommunityDeletedNotificationResponse of(CommunityDeletedNotification notification) {
		return CommunityDeletedNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.communityName(notification.getCommunityName())
				.build();
	}

}
