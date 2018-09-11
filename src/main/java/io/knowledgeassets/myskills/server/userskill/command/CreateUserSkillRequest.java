package io.knowledgeassets.myskills.server.userskill.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserSkillRequest {

	private String skillId;

	@Size(max = 64)
	private String skillName;

	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
}
