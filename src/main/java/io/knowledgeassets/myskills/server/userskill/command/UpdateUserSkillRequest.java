package io.knowledgeassets.myskills.server.userskill.command;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UpdateUserSkillRequest {

	@Min(0)
	@Max(4)
	private Integer currentLevel;

	@Min(0)
	@Max(4)
	private Integer desiredLevel;

	@Min(0)
	@Max(4)
	private Integer priority;
}
