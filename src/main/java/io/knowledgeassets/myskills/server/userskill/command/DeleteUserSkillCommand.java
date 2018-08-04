package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.userskill.command.UserSkillAggregate.UserSkillAggregateKey;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DeleteUserSkillCommand {
	@TargetAggregateIdentifier
	private final UserSkillAggregateKey id;

	public DeleteUserSkillCommand(UserSkillAggregateKey id) {
		this.id = id;
	}

	public UserSkillAggregateKey getId() {
		return id;
	}
}
