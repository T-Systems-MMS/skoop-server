package io.knowledgeassets.myskills.server.user.command;

public class CreateUserCommand {
	private final String userName;
	private final String firstName;
	private final String lastName;
	private final String email;

	public CreateUserCommand(String userName, String firstName, String lastName, String email) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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
