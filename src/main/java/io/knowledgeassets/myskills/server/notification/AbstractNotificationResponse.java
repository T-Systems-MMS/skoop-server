package io.knowledgeassets.myskills.server.notification;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.knowledgeassets.myskills.server.communityuser.registration.notification.CommunityUserRegistrationNotificationResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = CommunityUserRegistrationNotificationResponse.class)
})
@ApiModel(
		value = "AbstractNotificationResponse",
		description = "This holds notification data. It is used to transfer notification data to a client."
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
