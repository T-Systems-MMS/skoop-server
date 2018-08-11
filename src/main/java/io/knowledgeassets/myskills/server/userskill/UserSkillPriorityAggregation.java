package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.Skill;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@QueryResult
public class UserSkillPriorityAggregation {
	private Skill skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
}
