package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserSkillPriorityAggregationReportResponse",
		description = "Priority aggregation report for particular skill"
)
public class UserSkillPriorityAggregationReportResponse {
	private String id;
	@ApiModelProperty("Average priority of the skill")
	private Double averagePriority;
	@ApiModelProperty("Maximum priority of the skill")
	private Double maximumPriority;
	@ApiModelProperty("Number of users related to the skill with some priority")
	private Integer userCount;
	private String skillName;
	private String skillDescription;
}
