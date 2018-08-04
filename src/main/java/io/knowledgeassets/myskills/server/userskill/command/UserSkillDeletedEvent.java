package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.userskill.command.UserSkillAggregate.UserSkillAggregateKey;

import java.io.Serializable;

public class UserSkillDeletedEvent implements Serializable {
	private final UserSkillAggregateKey id;

	public UserSkillDeletedEvent(UserSkillAggregateKey id) {
		this.id = id;
	}

	public UserSkillAggregateKey getId() {
		return id;
	}
}
