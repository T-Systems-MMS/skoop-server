package io.knowledgeassets.myskills.server.user;

public class UserResponse {
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserResponse id(String id) {
		this.id = id;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserResponse userName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserResponse firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserResponse lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserResponse email(String email) {
		this.email = email;
		return this;
	}
}
