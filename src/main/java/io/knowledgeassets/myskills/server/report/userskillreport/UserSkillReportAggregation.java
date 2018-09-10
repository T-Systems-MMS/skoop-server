package io.knowledgeassets.myskills.server.report.userskillreport;

import io.knowledgeassets.myskills.server.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.StartNode;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class UserSkillReportAggregation {
	private String id;
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
}
