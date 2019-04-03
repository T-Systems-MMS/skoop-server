package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUserRoleChangedNotification extends Notification {

	@Property(name = "communityName")
	private String communityName;
	@Property(name = "role")
	private CommunityRole role;
	@Relationship(type = "USER")
	private User user;

	@Builder
	public CommunityUserRoleChangedNotification(String id, LocalDateTime creationDatetime, String communityName, CommunityRole role, User user) {
		super(id, creationDatetime);
		this.communityName = communityName;
		this.role = role;
		this.user = user;
	}

}
