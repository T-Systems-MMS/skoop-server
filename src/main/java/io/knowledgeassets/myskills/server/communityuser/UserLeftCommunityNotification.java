package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLeftCommunityNotification extends Notification {

	@Relationship(type = "USER")
	private User user;
	@Relationship(type = "COMMUNITY")
	private Community community;

	@Builder
	public UserLeftCommunityNotification(String id, LocalDateTime creationDatetime, User user, Community community) {
		super(id, creationDatetime);
		this.user = user;
		this.community = community;
	}
}
