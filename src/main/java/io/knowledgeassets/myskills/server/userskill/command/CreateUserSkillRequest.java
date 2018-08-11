package io.knowledgeassets.myskills.server.userskill.command;

public class CreateUserSkillRequest {
	private String skillId;
	private String skillName;
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;

	public String getSkillId() {
		return skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Integer getDesiredLevel() {
		return desiredLevel;
	}

	public void setDesiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
