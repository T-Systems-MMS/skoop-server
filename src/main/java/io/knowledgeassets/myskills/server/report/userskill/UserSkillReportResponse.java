package io.knowledgeassets.myskills.server.report.userskill;

import io.knowledgeassets.myskills.server.report.skill.SkillReportSimpleResponse;
import io.knowledgeassets.myskills.server.report.user.UserReportSimpleResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserSkillReportResponse",
		description = "Report of a relationship between a specific user and a specific skill."
)
public class UserSkillReportResponse {
	@ApiModelProperty("Unique ID of the report (not the original ID of the relationship)")
	private String id;

	@ApiModelProperty("Current skill level of the user")
	private Integer currentLevel;

	@ApiModelProperty("Desired skill level of the user")
	private Integer desiredLevel;

	@ApiModelProperty("Priority to reach the desired level")
	private Integer priority;

	@ApiModelProperty("Simple view of the related skill")
	private SkillReportSimpleResponse skill;

	@ApiModelProperty("Simple view of the related user")
	private UserReportSimpleResponse user;
}
