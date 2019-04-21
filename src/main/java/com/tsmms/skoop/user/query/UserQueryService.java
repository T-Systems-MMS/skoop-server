package com.tsmms.skoop.user.query;

import com.tsmms.skoop.exception.EmptyInputException;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@Service
public class UserQueryService {
	private UserRepository userRepository;

	public UserQueryService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsers() {
		return stream(userRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsersBySearchTerm(String search) {
		return stream(userRepository.findBySearchTerm(search).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserById(String userId) {
		return userRepository.findById(userId);
	}

	@Transactional(readOnly = true)
	public Stream<User> getUsersByIds(List<String> userIds) {
		return stream(userRepository.findAllById(userIds).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<User> getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "userIds", unless = "#result == null")
	public Optional<String> getUserIdByUserName(String userName) {
		return userRepository.findUserIdByUserName(userName);
	}

	@Transactional(readOnly = true)
	public boolean exists(String userId) {
		if (userId == null) {
			throw EmptyInputException.builder()
					.message("userId is null.")
					.build();
		}
		return userRepository.existsById(userId);
	}
}
