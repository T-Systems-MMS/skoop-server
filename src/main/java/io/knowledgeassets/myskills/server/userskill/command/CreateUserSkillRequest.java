package io.knowledgeassets.myskills.server.userskill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@ApiModel(value = "CreateUserSkillRequest"
		, description = "Request object for Creating a new relationship between a user and a skill. " +
		"UserId will be provided by PathVariable in Controller")
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserSkillRequest {

	@ApiModelProperty("Skill id")
	private String skillId;

	@ApiModelProperty("Skill name")
	@Size(max = 64)
	private String skillName;

	@ApiModelProperty("Current skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	@Min(0)
	@Max(4)
	private Integer currentLevel;

	@ApiModelProperty("Desired skill level of the user (range: 0-4 where 0 = no knowledge at all, 4 = expert")
	@Min(0)
	@Max(4)
	private Integer desiredLevel;

	@ApiModelProperty("Priority to reach the desired level (range: 0-4 where 0 = no action needed, 4 = urgent")
	@Min(0)
	@Max(4)
	private Integer priority;

}
