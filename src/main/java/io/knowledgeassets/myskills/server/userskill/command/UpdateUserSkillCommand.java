package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.userskill.command.UserSkillAggregate.UserSkillAggregateKey;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class UpdateUserSkillCommand {
	@TargetAggregateIdentifier
	private final UserSkillAggregateKey id;
	private final Integer currentLevel;
	private final Integer desiredLevel;
	private final Integer priority;

	public UpdateUserSkillCommand(UserSkillAggregateKey id, Integer currentLevel, Integer desiredLevel, Integer priority) {
		this.id = id;
		this.currentLevel = currentLevel;
		this.desiredLevel = desiredLevel;
		this.priority = priority;
	}

	public UserSkillAggregateKey getId() {
		return id;
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
