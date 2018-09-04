package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.knowledgeassets.myskills.server.report.userskillprioritydetailsreport.UserSkillPriorityDetailsReport;
import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.Set;

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
	private LocalDateTime date;

	@EqualsAndHashCode.Exclude
	@Relationship(type = "HAVE")
	private Set<UserSkillPriorityDetailsReport> userSkillPriorityDetailReports;

}
