package io.knowledgeassets.myskills.server.userskill.command;

import lombok.Data;

@Data
public class UpdateUserSkillRequest {
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
}
