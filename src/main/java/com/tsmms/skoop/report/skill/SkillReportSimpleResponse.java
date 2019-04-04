package com.tsmms.skoop.report.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "SkillReportSimpleResponse",
		description = "Simple view of skill data within a report."
)
public class SkillReportSimpleResponse {
	@ApiModelProperty("Name of the skill")
	private String name;

	@ApiModelProperty("Description of the skill")
	private String description;
}
