package com.tsmms.skoop.userskill;

import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserSkillResponse",
		description = "Relationship to a skill from a user perspective including the skill details, the current " +
				"skill level of the user, the desired skill level of the user and the priority to reach the desired " +
				"level."
)
public class UserSkillResponse {
	@ApiModelProperty("Details of the related skill")
	private SkillResponse skill;

	@ApiModelProperty("Current skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert)")
	private Integer currentLevel;

	@ApiModelProperty("Desired skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert)")
	private Integer desiredLevel;

	@ApiModelProperty("Priority to reach the desired level (range: 0-4 where 0 = no action needed, 4 = urgent)")
	private Integer priority;

	@ApiModelProperty("The flag to indicate if the skill is a favourite one.")
	private boolean favourite;

	public static UserSkillResponse of(UserSkill userSkill) {
		return UserSkillResponse.builder()
				.skill(SkillResponse.of(userSkill.getSkill()))
				.currentLevel(userSkill.getCurrentLevel())
				.desiredLevel(userSkill.getDesiredLevel())
				.priority(userSkill.getPriority())
				.favourite(userSkill.isFavourite())
				.build();
	}

}
