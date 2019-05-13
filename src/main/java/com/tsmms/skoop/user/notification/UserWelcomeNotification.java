package com.tsmms.skoop.user.notification;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWelcomeNotification extends Notification {

	@Relationship(type = "RECIPIENT")
	private User user;

	@Builder
	public UserWelcomeNotification(String id, LocalDateTime creationDatetime, User user) {
		super(id, creationDatetime);
		this.user = user;
	}
}
