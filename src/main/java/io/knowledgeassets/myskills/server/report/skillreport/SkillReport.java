package io.knowledgeassets.myskills.server.report.skillreport;

import io.knowledgeassets.myskills.server.report.skillpriorityreport.SkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
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
public class SkillReport {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;

	@EqualsAndHashCode.Exclude
	@Relationship(type = "REPORT_SKILL_RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<SkillPriorityReport> skillPriorityReports;

	@EqualsAndHashCode.Exclude
	@Relationship(type = "SKILL_USER_RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkillReport> userSkillReports;
}
