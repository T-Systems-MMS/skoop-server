package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.user.UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class UserCommandService {
	private UserRepository userRepository;

	public UserCommandService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * The default value of coach field for newly created users (created during login) must be "do not show as coach".
	 *
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	@Transactional
	public User createUser(String userName, String firstName, String lastName, String email) {
		userRepository.findByUserName(userName).ifPresent(user -> {
			throw new IllegalArgumentException(format("User with name '%s' already exists", userName));
		});
		return userRepository.save(User.builder()
				.id(UUID.randomUUID().toString())
				.userName(userName)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.coach(false)
				.build());
	}

	/**
	 * The "userName", "firstName", "lastName" and "email" properties are read-only,
	 * because these values are obtained from the OAuth2 identity token. So we only update other fields.
	 *
	 * @param id
	 * @param userRequest
	 * @return
	 */
	@Transactional
	public User updateUser(String id, UserRequest userRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("User with ID '%s' not found", id)));
		user.setCoach(userRequest.getCoach());
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("User with ID '%s' not found", id)));
		userRepository.delete(user);
	}

}
