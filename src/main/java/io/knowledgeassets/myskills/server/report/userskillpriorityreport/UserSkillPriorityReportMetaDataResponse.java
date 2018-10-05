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
@ApiModel(
		value = "UserSkillPriorityReportMetaDataResponse",
		description = "Meta data view of a user skill priority report."
)
public class UserSkillPriorityReportMetaDataResponse {
	@ApiModelProperty("Report id")
	private String id;
	@ApiModelProperty("Report created date.")
	private LocalDateTime date;
	@ApiModelProperty("The number of skills in this report")
	private Integer skillCount;
}
