package io.knowledgeassets.myskills.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserIdentity extends User {
	private final String userId;
	private final String userName;
	private final String firstName;
	private final String lastName;
	private final String email;

	public UserIdentity(String userId, String userName, String firstName, String lastName, String email,
						String password, boolean enabled, boolean accountNonExpired,
						boolean credentialsNonExpired, boolean accountNonLocked,
						Collection<? extends GrantedAuthority> authorities) {
		super(userName, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getUserId() {
		return userId;
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
