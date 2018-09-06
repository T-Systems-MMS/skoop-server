package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSkillPriorityReportResponse {
	private String id;
	private LocalDateTime date;
	private Long skillCount;
}
