package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport;

import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserSkillPriorityAggregationReport {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "averagePriority")
	private Double averagePriority;
	@Property(name = "maximumPriority")
	private Double maximumPriority;
	@Property(name = "userCount")
	private Integer userCount;
	@Property(name = "skillName")
	private String skillName;
	@Property(name = "skillDescription")
	private String skillDescription;

	@EqualsAndHashCode.Exclude
	private List<UserSkillReport> userSkillReports;

}
