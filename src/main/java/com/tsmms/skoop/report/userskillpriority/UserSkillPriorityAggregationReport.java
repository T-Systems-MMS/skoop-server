package com.tsmms.skoop.report.userskillpriority;

import com.tsmms.skoop.report.userskill.UserSkillReport;
import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

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
	@Relationship(type = "CONTAINS")
	private List<UserSkillReport> userSkillReports;
}
