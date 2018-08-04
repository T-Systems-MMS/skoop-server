package io.knowledgeassets.myskills.server.skill.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class UpdateSkillCommand {
	@TargetAggregateIdentifier
	private final String id;
	private final String name;
	private final String description;

	public UpdateSkillCommand(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
