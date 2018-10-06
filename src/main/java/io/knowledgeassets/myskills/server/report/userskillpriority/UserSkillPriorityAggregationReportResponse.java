package io.knowledgeassets.myskills.server.report.userskillpriority;

import io.knowledgeassets.myskills.server.report.skill.SkillReportSimpleResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserSkillPriorityAggregationReportResponse",
		description = "Report of the priority aggregation for a particular skill"
)
public class UserSkillPriorityAggregationReportResponse {
	@ApiModelProperty("Unique ID of the aggregation report")
	private String id;

	@ApiModelProperty("Average priority rating of the skill")
	private Double averagePriority;

	@ApiModelProperty("Maximum priority rating of the skill")
	private Double maximumPriority;

	@ApiModelProperty("Number of users related to the skill with some priority")
	private Integer userCount;

	@ApiModelProperty("Simple view of the skill represented by this report")
	private SkillReportSimpleResponse skill;
}
