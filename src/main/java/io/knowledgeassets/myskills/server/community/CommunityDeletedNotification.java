package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDeletedNotification extends Notification {

	@Property(name = "communityName")
	private String communityName;

	@Relationship(type = "RECIPIENT")
	private List<User> recipients;

	@Builder
	public CommunityDeletedNotification(String id, LocalDateTime creationDatetime, String communityName, List<User> recipients) {
		super(id, creationDatetime);
		this.communityName = communityName;
		this.recipients = recipients;
	}
}
