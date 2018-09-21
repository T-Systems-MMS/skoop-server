package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@ApiModel(value = "Details of a specific report",
		description = "This holds the skills statistic like (averagePriority and maximumPriority) for a specific report.")
public class UserSkillPriorityReportDetailsResponse {
	private String id;
	@ApiModelProperty("Average priority of the skill")
	private Double averagePriority;
	@ApiModelProperty("Maximum priority of the skill")
	private Double maximumPriority;
	@ApiModelProperty("The number of users that are considered in these statistics")
	private Integer userCount;
	@ApiModelProperty("Details of the skill")
	private SkillResponse skill;

}
