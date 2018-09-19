package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report class.
 * We save Report object by Batch Job Scheduling.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserSkillPriorityReport {

	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "date")
	@Index(unique = true)
	private LocalDateTime date;

	@EqualsAndHashCode.Exclude
	private List<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReports;

}
