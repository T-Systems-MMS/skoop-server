package com.tsmms.skoop.notification;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@ApiModel(
		value = "AbstractNotificationResponse",
		description = "This holds notification data. It is used to transfer notification data to a client. The field \"type\" is not documented here as it is added by Jackson automatically. " +
				" The type field indicates actual type the notification belongs to. " +
				" The possible values of the type field are: RequestToJoinCommunityNotificationResponse, InvitationToJoinCommunityNotificationResponse, AcceptanceToCommunityNotificationResponse."
)
public abstract class AbstractNotificationResponse {

	@ApiModelProperty("Identifier of the notification.")
	private String id;

	@ApiModelProperty("Datetime when notification was created")
	private LocalDateTime creationDatetime;

	public AbstractNotificationResponse() {}

	public AbstractNotificationResponse(String id, LocalDateTime creationDatetime) {
		this.id = id;
		this.creationDatetime = creationDatetime;
	}

}
