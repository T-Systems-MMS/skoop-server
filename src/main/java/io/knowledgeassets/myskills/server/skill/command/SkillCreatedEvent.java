package io.knowledgeassets.myskills.server.skill.command;

import java.io.Serializable;

public class SkillCreatedEvent implements Serializable {
	private final String id;
	private final String name;
	private final String description;

	public SkillCreatedEvent(String id, String name, String description) {
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
