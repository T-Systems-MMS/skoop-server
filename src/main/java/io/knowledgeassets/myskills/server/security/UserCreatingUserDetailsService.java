package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import io.knowledgeassets.myskills.server.user.query.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

/**
 * Implementation of the Spring {@link org.springframework.security.core.userdetails.UserDetailsService} which
 * automatically creates users in the database upon successful authentication.
 * <p>
 * The implementation depends on the {@link UserQueryService} and {@link UserCommandService} to allow lookup of the
 * authenticated user and creation of new users.
 * </p>
 */
public class UserCreatingUserDetailsService extends InMemoryUserDetailsManager {
	private UserQueryService userQueryService;
	private UserCommandService userCommandService;

	public UserCreatingUserDetailsService(Properties users, UserQueryService userQueryService,
										  UserCommandService userCommandService) {
		super(users);
		this.userQueryService = userQueryService;
		this.userCommandService = userCommandService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = super.loadUserByUsername(username);
		User user = userQueryService.getUserByUserName(username)
				.orElseGet(() -> userCommandService.createUser(username));
		return new UserIdentity(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(),
				user.getEmail(), userDetails.getPassword(), userDetails.isEnabled(), userDetails.isAccountNonExpired(),
				userDetails.isCredentialsNonExpired(), userDetails.isAccountNonLocked(), userDetails.getAuthorities());
	}
}
