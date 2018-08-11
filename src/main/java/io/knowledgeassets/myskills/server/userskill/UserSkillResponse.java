package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserSkillResponse",
		description = "Relationship to a skill from a user perspective including the skill details, the current skill " +
				"level of the user, the desired skill level of the user and the priority to reach the desired level.")
public class UserSkillResponse {
	@ApiModelProperty("Details of the related skill")
	private SkillResponse skill;
	@ApiModelProperty("Current skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	private Integer currentLevel;
	@ApiModelProperty("Desired skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	private Integer desiredLevel;
	@ApiModelProperty("Priority to reach the desired level (range: 0-4 where 0 = no action needed, 4 = urgent")
	private Integer priority;

	public UserSkillResponse skill(SkillResponse skill) {
		this.skill = skill;
		return this;
	}

	public UserSkillResponse currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public UserSkillResponse desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public UserSkillResponse priority(Integer priority) {
		this.priority = priority;
		return this;
	}
}
