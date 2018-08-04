package io.knowledgeassets.myskills.server.user.command;

import java.io.Serializable;

public class UserDeletedEvent implements Serializable {
	private final String id;

	public UserDeletedEvent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
