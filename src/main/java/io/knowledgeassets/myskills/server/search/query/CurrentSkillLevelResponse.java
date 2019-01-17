package io.knowledgeassets.myskills.server.search.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(value = "A pair of a skill name and current level of the skill.",
		description = "A pair of a skill name and current level of the skill.")
public class CurrentSkillLevelResponse {

	@ApiModelProperty("Skill name")
	@NotEmpty
	private String skillName;
	@ApiModelProperty("Current level of a skill")
	@NotNull
	private Integer currentLevel;

}
