package com.tsmms.skoop.user.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.user.UserRequest;
import com.tsmms.skoop.user.notification.UserWelcomeNotification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
public class UserCommandService {

	private final UserRepository userRepository;
	private final NotificationCommandService notificationCommandService;

	public UserCommandService(UserRepository userRepository,
							  NotificationCommandService notificationCommandService) {
		this.userRepository = requireNonNull(userRepository);
		this.notificationCommandService = requireNonNull(notificationCommandService);
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
		final User user = userRepository.save(User.builder()
				.id(UUID.randomUUID().toString())
				.referenceId(UUID.randomUUID().toString())
				.userName(userName)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.build());
		notificationCommandService.save(UserWelcomeNotification.builder()
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.user(user)
				.build()
		);
		return user;
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
