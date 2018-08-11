package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.user.UserResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SkillUserResponse",
		description = "Relationship to a user from a skill perspective including the user details, the current skill " +
				"level of the user, the desired skill level of the user and the priority to reach the desired level.")
public class SkillUserResponse {
	@ApiModelProperty("Details of the related user")
	private UserResponse user;
	@ApiModelProperty("Current skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	private Integer currentLevel;
	@ApiModelProperty("Desired skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	private Integer desiredLevel;
	@ApiModelProperty("Priority to reach the desired level (range: 0-4 where 0 = no action needed, 4 = urgent")
	private Integer priority;

	public SkillUserResponse user(UserResponse user) {
		this.user = user;
		return this;
	}

	public SkillUserResponse currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public SkillUserResponse desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public SkillUserResponse priority(Integer priority) {
		this.priority = priority;
		return this;
	}
}
