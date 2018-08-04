package io.knowledgeassets.myskills.server.skill.command;

public class CreateSkillCommand {
	private final String name;
	private final String description;

	public CreateSkillCommand(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
