package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Report (UserSkillPriorityReport)",
		description = "This holds information of a report. It will be used for sending report information to client.")
public class UserSkillPriorityReportResponse {
	@ApiModelProperty("Report id")
	private String id;
	@ApiModelProperty("Report created date.")
	private LocalDateTime date;
	@ApiModelProperty("The number of skills for this report")
	private Integer skillCount;
}
