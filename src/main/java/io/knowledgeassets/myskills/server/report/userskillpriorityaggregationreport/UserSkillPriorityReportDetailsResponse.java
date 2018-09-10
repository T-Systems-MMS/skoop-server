package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import lombok.*;

@Data
@Builder
public class UserSkillPriorityReportDetailsResponse {
	private String id;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
	private SkillResponse skill;

}
