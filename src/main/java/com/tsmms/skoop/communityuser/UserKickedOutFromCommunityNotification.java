package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.notification.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKickedOutFromCommunityNotification extends Notification {

	@Relationship(type = "USER")
	private User user;
	@Relationship(type = "COMMUNITY")
	private Community community;

	@Builder
	public UserKickedOutFromCommunityNotification(String id, LocalDateTime creationDatetime, User user, Community community) {
		super(id, creationDatetime);
		this.user = user;
		this.community = community;
	}
}
