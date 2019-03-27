package io.knowledgeassets.myskills.server.notification;

import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Notification {

	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "type")
	private NotificationType type;

	@Property(name = "creationDatetime")
	private LocalDateTime creationDatetime;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "ATTACHED_TO")
	private CommunityUserRegistration registration;

}
