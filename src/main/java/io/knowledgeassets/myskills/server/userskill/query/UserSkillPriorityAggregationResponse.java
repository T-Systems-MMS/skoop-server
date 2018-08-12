package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSkillPriorityAggregationResponse {
	private SkillResponse skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
}
