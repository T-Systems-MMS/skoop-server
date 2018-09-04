package io.knowledgeassets.myskills.server.report.userskillprioritydetailsreport;

import lombok.*;

@Data
@Builder
public class UserSkillPriorityReportDetailsResponse {
	private String id;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
	private String skillName;

}
