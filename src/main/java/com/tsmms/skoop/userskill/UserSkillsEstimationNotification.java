package com.tsmms.skoop.userskill;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillsEstimationNotification extends Notification {

	@Relationship(type = "SKILL")
	private Set<Skill> skills;

	@Relationship(type = "RECIPIENT")
	private User user;

	@Builder
	public UserSkillsEstimationNotification(String id, LocalDateTime creationDatetime, Set<Skill> skills, User user) {
		super(id, creationDatetime);
		this.skills = skills;
		this.user = user;
	}
}
