package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillPriorityReportResponse {
	private String id;
	private LocalDateTime date;
	private Integer skillCount;
}
