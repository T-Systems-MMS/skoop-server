package io.knowledgeassets.myskills.server.userskill.command;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CreateUserSkillRequest {

	private String skillId;

	@Size(max = 64)
	private String skillName;

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
