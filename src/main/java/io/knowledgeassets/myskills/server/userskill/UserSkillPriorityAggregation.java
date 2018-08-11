package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.Skill;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class UserSkillPriorityAggregation {
	private Skill skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Double getAveragePriority() {
		return averagePriority;
	}

	public void setAveragePriority(Double averagePriority) {
		this.averagePriority = averagePriority;
	}

	public Double getMaximumPriority() {
		return maximumPriority;
	}

	public void setMaximumPriority(Double maximumPriority) {
		this.maximumPriority = maximumPriority;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
}
