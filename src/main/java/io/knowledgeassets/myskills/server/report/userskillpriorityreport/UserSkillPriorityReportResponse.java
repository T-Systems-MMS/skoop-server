package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ApiModel(
		value = "UserSkillPriorityReportMetaDataResponse",
		description = "Meta data view of a user skill priority report."
)
public class UserSkillPriorityReportResponse {
	@ApiModelProperty("Report id")
	private String id;
	@ApiModelProperty("Report created date.")
	private LocalDateTime date;
	@ApiModelProperty("Priority aggregation reports for all skills.")
	private List<UserSkillPriorityAggregationReportResponse> aggregationReports;
}
