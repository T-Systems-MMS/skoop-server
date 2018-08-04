package io.knowledgeassets.myskills.server.skill.command;

import java.io.Serializable;

public class SkillDeletedEvent implements Serializable {
	private final String id;

	public SkillDeletedEvent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
