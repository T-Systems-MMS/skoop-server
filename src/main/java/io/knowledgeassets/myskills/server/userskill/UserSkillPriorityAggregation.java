package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class UserSkillPriorityAggregation {
	private Skill skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
}
