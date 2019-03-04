package io.knowledgeassets.myskills.server.usernotification;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserNotification {

	@Id
	@Property(name = "id")
	private String id;

	@Relationship(type = "INITIATED_BY", direction = Relationship.INCOMING)
	private User initiator;
	@Relationship(type = "RELATES_TO")
	private Community community;
	@Relationship(type = "SENT_TO")
	private User recipient;
	@Property(name = "creationDatetime")
	@NotNull
	private LocalDateTime creationDatetime;
	@Property(name = "status")
	private UserNotificationStatus status;
	@Property(name = "type")
	@NotNull
	private UserNotificationType type;

}
