package com.tsmms.skoop.userskill.query;

import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "Skill priority statistics",
		description = "This holds the skills statistic like (averagePriority and maximumPriority).")
public class UserSkillPriorityAggregationResponse {
	@ApiModelProperty("Details of the skill")
	private SkillResponse skill;
	@ApiModelProperty("Average priority of the skill")
	private Double averagePriority;
	@ApiModelProperty("Maximum priority of the skill")
	private Double maximumPriority;
	@ApiModelProperty("The number of users that are considered in these statistics")
	private Integer userCount;
}
