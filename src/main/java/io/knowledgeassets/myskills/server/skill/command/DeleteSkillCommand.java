package io.knowledgeassets.myskills.server.skill.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DeleteSkillCommand {
	@TargetAggregateIdentifier
	private final String id;

	public DeleteSkillCommand(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
