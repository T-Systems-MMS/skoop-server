package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserQueryService {
	private UserRepository userRepository;

	public UserQueryService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsers() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserById(String userId) {
		return userRepository.findById(userId);
	}

	@Transactional(readOnly = true)
	public Optional<User> getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Transactional(readOnly = true)
	public Optional<User> getById(String userId) {
		return userRepository.findById(userId);
	}
}
