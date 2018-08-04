package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.SkillResponse;

public class UserSkillPriorityAggregationResponse {
	private SkillResponse skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;

	public SkillResponse getSkill() {
		return skill;
	}

	public void setSkill(SkillResponse skill) {
		this.skill = skill;
	}

	public UserSkillPriorityAggregationResponse skill(SkillResponse skill) {
		this.skill = skill;
		return this;
	}

	public Double getAveragePriority() {
		return averagePriority;
	}

	public void setAveragePriority(Double averagePriority) {
		this.averagePriority = averagePriority;
	}

	public UserSkillPriorityAggregationResponse averagePriority(Double averagePriority) {
		this.averagePriority = averagePriority;
		return this;
	}

	public Double getMaximumPriority() {
		return maximumPriority;
	}

	public void setMaximumPriority(Double maximumPriority) {
		this.maximumPriority = maximumPriority;
	}

	public UserSkillPriorityAggregationResponse maximumPriority(Double maximumPriority) {
		this.maximumPriority = maximumPriority;
		return this;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public UserSkillPriorityAggregationResponse userCount(Integer userCount) {
		this.userCount = userCount;
		return this;
	}
}
