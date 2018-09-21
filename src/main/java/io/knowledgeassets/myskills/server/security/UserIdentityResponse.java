package io.knowledgeassets.myskills.server.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "UserIdentityResponse",
		description = "This holds the identity details of the authenticated user. It will be used for sending user details to client.")
public class UserIdentityResponse {

	@ApiModelProperty("User id")
	private String userId;
	@ApiModelProperty("UserName of the authenticated user. It can not be null.")
	private String userName;
	@ApiModelProperty("First name of the authenticated user.")
	private String firstName;
	@ApiModelProperty("Last name of the authenticated user.")
	private String lastName;
	@ApiModelProperty("Email of the authenticated user.")
	private String email;
	@ApiModelProperty("Authorities of the authenticated user.")
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
