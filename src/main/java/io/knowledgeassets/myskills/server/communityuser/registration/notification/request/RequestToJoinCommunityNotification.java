package io.knowledgeassets.myskills.server.communityuser.registration.notification.request;

import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.notification.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestToJoinCommunityNotification extends Notification {

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "CAUSED_BY")
	private CommunityUserRegistration registration;

	@Builder
	public RequestToJoinCommunityNotification(String id, LocalDateTime creationDatetime, CommunityUserRegistration registration) {
		super(id, creationDatetime);
		this.registration = registration;
	}

}
