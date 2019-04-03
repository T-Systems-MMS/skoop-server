package io.knowledgeassets.myskills.server.communityuser;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.knowledgeassets.myskills.server.community.CommunityResponse;
import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
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
		value = "UserKickedOutFromCommunityNotificationResponse",
		description = "This holds notification data about user kicked out event. " +
				"It is used to transfer user kicked out notification data to a client."
)
@JsonTypeName("UserKickedOutFromCommunityNotification")
public class UserKickedOutFromCommunityNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("The community user kicked from.")
	private CommunityResponse community;

	@Builder
	public UserKickedOutFromCommunityNotificationResponse(String id, LocalDateTime creationDatetime, CommunityResponse community) {
		super(id, creationDatetime);
		this.community = community;
	}

	public static UserKickedOutFromCommunityNotificationResponse of(UserKickedOutFromCommunityNotification notification) {
		return UserKickedOutFromCommunityNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.community(CommunityResponse.of(notification.getCommunity()))
				.build();
	}

}
