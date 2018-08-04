package io.knowledgeassets.myskills.server.user;

import java.util.List;

public class UserIdentityResponse {
	private String userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserIdentityResponse userId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserIdentityResponse userName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserIdentityResponse firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserIdentityResponse lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserIdentityResponse email(String email) {
		this.email = email;
		return this;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public UserIdentityResponse roles(List<String> roles) {
		this.roles = roles;
		return this;
	}
}
