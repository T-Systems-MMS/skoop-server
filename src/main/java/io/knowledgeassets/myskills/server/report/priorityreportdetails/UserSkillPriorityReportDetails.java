package io.knowledgeassets.myskills.server.report.priorityreportdetails;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserSkillPriorityReportDetails {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "averagePriority")
	private Double averagePriority;
	@Property(name = "maximumPriority")
	private Double maximumPriority;
	@Property(name = "userCount")
	private Integer userCount;

	@EqualsAndHashCode.Exclude
	@Relationship(type = "HAVE_SKILL")
	private SkillReport skillReport;
	@EqualsAndHashCode.Exclude
	@Relationship(type = "HAVE_USERS")
	private Set<UserReport> userReports;

}
