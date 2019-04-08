package com.tsmms.skoop.communityuser.registration;

import com.tsmms.skoop.notification.Notification;
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
public class InvitationToJoinCommunityNotification extends Notification {

	@Relationship(type = "CAUSED_BY")
	private CommunityUserRegistration registration;

	@Property(name = "communityName")
	private String communityName;

	@Builder
	public InvitationToJoinCommunityNotification(String id, LocalDateTime creationDatetime, CommunityUserRegistration registration,
												 String communityName) {
		super(id, creationDatetime);
		this.registration = registration;
		this.communityName = communityName;
	}
}
