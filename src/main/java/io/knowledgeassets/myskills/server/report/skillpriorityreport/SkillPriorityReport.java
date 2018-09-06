package io.knowledgeassets.myskills.server.report.skillpriorityreport;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import lombok.*;
import org.neo4j.ogm.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "REPORT_SKILL_RELATED_TO")
public class SkillPriorityReport {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "averagePriority")
	private Double averagePriority;
	@Property(name = "maximumPriority")
	private Double maximumPriority;
	@Property(name = "userCount")
	private Integer userCount;

	@StartNode
	private UserSkillPriorityReport userSkillPriorityReport;
	@EndNode
	private SkillReport skillReport;

}
