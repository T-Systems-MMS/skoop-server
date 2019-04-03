package com.tsmms.skoop.communityuser.registration;

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
public class AcceptanceToCommunityNotification extends Notification {

	@Relationship(type = "CAUSED_BY")
	private CommunityUserRegistration registration;

	@Builder
	public AcceptanceToCommunityNotification(String id, LocalDateTime creationDatetime, CommunityUserRegistration registration) {
		super(id, creationDatetime);
		this.registration = registration;
	}
}
