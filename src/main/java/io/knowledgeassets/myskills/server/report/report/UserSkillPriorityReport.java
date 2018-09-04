package io.knowledgeassets.myskills.server.report.report;

import io.knowledgeassets.myskills.server.report.priorityreportdetails.UserSkillPriorityReportDetails;
import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.Set;

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
	private Set<UserSkillPriorityReportDetails> userSkillPriorityReportDetails;

}
