package io.knowledgeassets.myskills.server.report.userskillreport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
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
	@Property(name = "skillName")
	private String skillName;
	@Property(name = "userName")
	private String userName;
}
