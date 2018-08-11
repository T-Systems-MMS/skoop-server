package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
public class UserCommandService {
	private UserRepository userRepository;

	public UserCommandService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public User createUser(String userName) {
		return createUser(userName, null, null, null);
	}

	@Transactional
	public User createUser(String userName, String firstName, String lastName, String email) {
		// TODO: Check if user with given name already exists.
		return userRepository.save(new User().newId().userName(userName).firstName(firstName).lastName(lastName)
				.email(email));
	}

	@Transactional
	public User updateUser(String id, String userName, String firstName, String lastName, String email) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("User with ID '%s' not found", id)));
		user.setUserName(userName);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("User with ID '%s' not found", id)));
		userRepository.delete(user);
	}
}
