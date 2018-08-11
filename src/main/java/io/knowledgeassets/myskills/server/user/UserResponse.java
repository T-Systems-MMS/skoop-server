package io.knowledgeassets.myskills.server.user;

import lombok.Data;

@Data
public class UserResponse {
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;

	public UserResponse id(String id) {
		this.id = id;
		return this;
	}

	public UserResponse userName(String userName) {
		this.userName = userName;
		return this;
	}

	public UserResponse firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public UserResponse lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public UserResponse email(String email) {
		this.email = email;
		return this;
	}
}
