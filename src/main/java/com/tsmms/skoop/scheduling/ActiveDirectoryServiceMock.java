package com.tsmms.skoop.scheduling;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The mock service to simulate interaction with Active Directory.
 */
@Service
public class ActiveDirectoryServiceMock {

	private final UserQueryService userQueryService;
	private final Random random = new Random();

	public ActiveDirectoryServiceMock(UserQueryService userQueryService) {
		this.userQueryService = requireNonNull(userQueryService);
	}

	/**
	 * Gets manager username by username of a subordinate.
	 * @param userName - username
	 * @return manager username
	 */
	public Optional<String> getManagerByUser(String userName) {
		if (userName == null) {
			throw new IllegalArgumentException("Username cannot be null.");
		}
		final Set<String> names = userQueryService.getUsers()
				.map(User::getUserName)
				.filter(managerUserName -> !managerUserName.equals(userName))
				.collect(Collectors.toSet());
		return names.stream().skip(random.nextInt(names.size())).findFirst();
	}

}
