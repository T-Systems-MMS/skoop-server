package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.user.UserResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

	public UserResponse getUser() {
		return user;
	}

	public void setUser(UserResponse user) {
		this.user = user;
	}

	public SkillUserResponse user(UserResponse user) {
		this.user = user;
		return this;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	public SkillUserResponse currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public Integer getDesiredLevel() {
		return desiredLevel;
	}

	public void setDesiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
	}

	public SkillUserResponse desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public SkillUserResponse priority(Integer priority) {
		this.priority = priority;
		return this;
	}
}
