package io.knowledgeassets.myskills.server.report.report;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.Property;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSkillPriorityReportResponse {
	private String id;
	private LocalDateTime date;
	private Integer skillCount;
}
