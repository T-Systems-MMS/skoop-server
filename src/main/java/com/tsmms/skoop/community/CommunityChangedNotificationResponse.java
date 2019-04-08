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
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "CommunityChangedNotificationResponse",
		description = "This holds notification data about community changed. " +
				"It is used to transfer community changed notification data to a client."
)
@JsonTypeName("CommunityChangedNotification")
public class CommunityChangedNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Community name.")
	private String communityName;

	@ApiModelProperty("Community details that have been changed. The possible values are NAME, TYPE, DESCRIPTION, LINKS, SKILLS.")
	private Set<CommunityDetails> communityDetails;

	@Builder
	public CommunityChangedNotificationResponse(String id, LocalDateTime creationDatetime,
												String communityName,
												Set<CommunityDetails> communityDetails) {
		super(id, creationDatetime);
		this.communityName = communityName;
		this.communityDetails = communityDetails;
	}

	public static CommunityChangedNotificationResponse of(CommunityChangedNotification notification) {
		return CommunityChangedNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.communityName(notification.getCommunityName())
				.communityDetails(notification.getCommunityDetails())
				.build();
	}

}
