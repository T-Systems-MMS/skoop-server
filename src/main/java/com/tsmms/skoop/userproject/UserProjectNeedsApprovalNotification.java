package com.tsmms.skoop.userproject;

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
public class UserProjectNeedsApprovalNotification extends Notification {

	@Relationship(type = "USER_PROJECT")
	private UserProject userProject;

	@Builder
	public UserProjectNeedsApprovalNotification(String id, LocalDateTime creationDatetime, UserProject userProject) {
		super(id, creationDatetime);
		this.userProject = userProject;
	}
}
