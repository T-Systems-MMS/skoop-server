package io.knowledgeassets.myskills.server.report.priorityreportdetails;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class UserSkillPriorityReportDetailsResponse {
	private String id;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
	private String skillName;

}
