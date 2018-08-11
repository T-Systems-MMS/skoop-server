package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import lombok.Data;

@Data
public class UserSkillPriorityAggregationResponse {
	private SkillResponse skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;

	public UserSkillPriorityAggregationResponse skill(SkillResponse skill) {
		this.skill = skill;
		return this;
	}

	public UserSkillPriorityAggregationResponse averagePriority(Double averagePriority) {
		this.averagePriority = averagePriority;
		return this;
	}

	public UserSkillPriorityAggregationResponse maximumPriority(Double maximumPriority) {
		this.maximumPriority = maximumPriority;
		return this;
	}

	public UserSkillPriorityAggregationResponse userCount(Integer userCount) {
		this.userCount = userCount;
		return this;
	}
}
