package io.knowledgeassets.myskills.server.report.userskillpriority;

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
		value = "UserSkillPriorityReportSimpleResponse",
		description = "Simple view of a report of skill priority ratings."
)
public class UserSkillPriorityReportSimpleResponse {
	@ApiModelProperty("Unique ID of the report")
	private String id;
	@ApiModelProperty("Creation date of the report")
	private LocalDateTime date;
	@ApiModelProperty("Number of skill ratings contained in the report")
	private Integer skillCount;
}
