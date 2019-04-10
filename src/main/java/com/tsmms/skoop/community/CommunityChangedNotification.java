package com.tsmms.skoop.community;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityChangedNotification extends Notification {

	@Property(name = "communityName")
	private String communityName;

	@Property(name = "communityDetails")
	private Set<CommunityDetails> communityDetails;

	@Relationship(type = "RECIPIENT")
	private List<User> recipients;

	@Relationship(type = "COMMUNITY")
	private Community community;

	@Builder
	public CommunityChangedNotification(String id, LocalDateTime creationDatetime,
										String communityName,
										Set<CommunityDetails> communityDetails,
										List<User> recipients,
										Community community) {
		super(id, creationDatetime);
		this.communityName = communityName;
		this.communityDetails = communityDetails;
		this.recipients = recipients;
		this.community = community;
	}

}
