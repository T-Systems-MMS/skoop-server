package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.user.UserRequest;
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
			throw DuplicateResourceException.builder()
					.message(format("User with name '%s' already exists", userName))
					.build();
		});
		return userRepository.save(User.builder()
				.id(UUID.randomUUID().toString())
				.referenceId(UUID.randomUUID().toString())
				.userName(userName)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
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
		User user = userRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		user.setAcademicDegree(userRequest.getAcademicDegree());
		user.setPositionProfile(userRequest.getPositionProfile());
		user.setSummary(userRequest.getSummary());
		user.setIndustrySectors(userRequest.getIndustrySectors());
		user.setSpecializations(userRequest.getSpecializations());
		user.setCertificates(userRequest.getCertificates());
		user.setLanguages(userRequest.getLanguages());
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(String id) {
		User user = userRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		userRepository.delete(user);
	}

}
