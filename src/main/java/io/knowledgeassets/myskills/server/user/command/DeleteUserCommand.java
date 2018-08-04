package io.knowledgeassets.myskills.server.user.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DeleteUserCommand {
	@TargetAggregateIdentifier
	private final String id;

	public DeleteUserCommand(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
