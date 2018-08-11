package io.knowledgeassets.myskills.server.userskill.command;

import lombok.Data;

@Data
public class CreateUserSkillRequest {
	private String skillId;
	private String skillName;
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
}
