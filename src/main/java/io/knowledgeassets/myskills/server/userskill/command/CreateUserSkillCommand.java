package io.knowledgeassets.myskills.server.userskill.command;

public class CreateUserSkillCommand {
	private final String userId;
	private final String skillId;
	private final Integer currentLevel;
	private final Integer desiredLevel;
	private final Integer priority;

	public CreateUserSkillCommand(String userId, String skillId, Integer currentLevel, Integer desiredLevel,
								  Integer priority) {
		this.userId = userId;
		this.skillId = skillId;
		this.currentLevel = currentLevel;
		this.desiredLevel = desiredLevel;
		this.priority = priority;
	}

	public String getUserId() {
		return userId;
	}

	public String getSkillId() {
		return skillId;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public Integer getDesiredLevel() {
		return desiredLevel;
	}

	public Integer getPriority() {
		return priority;
	}
}
