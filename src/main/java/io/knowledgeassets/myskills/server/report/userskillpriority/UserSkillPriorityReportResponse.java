package io.knowledgeassets.myskills.server.report.userskillpriority;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ApiModel(
		value = "UserSkillPriorityReportResponse",
		description = "Report of the aggregated priorities for all skills."
)
public class UserSkillPriorityReportResponse {
	@ApiModelProperty("Unique ID of the report")
	private String id;

	@ApiModelProperty("Creation data of the report")
	private LocalDateTime date;

	@ApiModelProperty("Aggregation reports for all skills")
	private List<UserSkillPriorityAggregationReportResponse> aggregationReports;
}
