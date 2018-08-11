package io.knowledgeassets.myskills.server.skill;

import lombok.Data;

@Data
public class SkillResponse {
	private String id;
	private String name;
	private String description;

	public SkillResponse id(String id) {
		this.id = id;
		return this;
	}

	public SkillResponse name(String name) {
		this.name = name;
		return this;
	}

	public SkillResponse description(String description) {
		this.description = description;
		return this;
	}
}
