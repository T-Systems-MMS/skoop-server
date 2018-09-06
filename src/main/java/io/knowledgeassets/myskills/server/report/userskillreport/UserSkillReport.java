package io.knowledgeassets.myskills.server.report.userskillreport;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "SKILL_USER_RELATED_TO")
public class UserSkillReport {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "currentLevel")
	private Integer currentLevel;
	@Property(name = "desiredLevel")
	private Integer desiredLevel;
	@Property(name = "priority")
	private Integer priority;

	@StartNode
	private SkillReport skillReport;
	@EndNode
	private UserReport userReport;
}
