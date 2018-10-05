package io.knowledgeassets.myskills.server.report.userskillreport;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSkillReportResponse {
	private String id;
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
	private String skillName;
	private String userName;
}
