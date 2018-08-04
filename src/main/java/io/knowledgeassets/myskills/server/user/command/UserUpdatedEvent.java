package io.knowledgeassets.myskills.server.user.command;

import java.io.Serializable;

public class UserUpdatedEvent implements Serializable {
	private final String id;
	private final String userName;
	private final String firstName;
	private final String lastName;
	private final String email;

	public UserUpdatedEvent(String id, String userName, String firstName, String lastName, String email) {
		this.id = id;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}
}
