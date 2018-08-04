package io.knowledgeassets.myskills.server.user.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserQueryService {
	private UserQueryRepository userQueryRepository;

	public UserQueryService(UserQueryRepository userQueryRepository) {
		this.userQueryRepository = userQueryRepository;
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsers() {
		return StreamSupport.stream(userQueryRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserById(String userId) {
		return userQueryRepository.findById(userId);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserByUserName(String userName) {
		return userQueryRepository.findByUserName(userName);
	}
}
