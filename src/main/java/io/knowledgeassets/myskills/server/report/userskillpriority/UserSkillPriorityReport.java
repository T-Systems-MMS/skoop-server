package io.knowledgeassets.myskills.server.report.userskillpriority;

import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report representing an invariable view of the user skill statistics aggregated by priority.
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
	@Relationship(type = "CONTAINS")
	private List<UserSkillPriorityAggregationReport> aggregationReports;
}
