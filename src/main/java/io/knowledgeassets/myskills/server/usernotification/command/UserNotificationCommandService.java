package io.knowledgeassets.myskills.server.usernotification.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.usernotification.UserNotification;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationRepository;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationStatus;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserNotificationCommandService {

	private final UserNotificationRepository userNotificationRepository;
	private final CurrentUserService currentUserService;

	public UserNotificationCommandService(UserNotificationRepository userNotificationRepository,
										  CurrentUserService currentUserService) {
		this.userNotificationRepository = userNotificationRepository;
		this.currentUserService = currentUserService;
	}

	/**
	 * Invite users to join a community.
	 * @param users - invited users
	 * @param community - community to join
	 * @return user invitations
	 */
	@Transactional
	public List<UserNotification> inviteUsers(List<User> users, Community community) {
		if (CollectionUtils.isEmpty(users)) {
			throw new IllegalArgumentException("There are no users no invite.");
		}
		final User currentUser = currentUserService.getCurrentUser();
		final LocalDateTime now = LocalDateTime.now();
		final List<UserNotification> invitations = users.stream().map(user -> UserNotification.builder()
				.community(community)
				.initiator(currentUser)
				.creationDatetime(now)
				.recipient(user)
				.id(UUID.randomUUID().toString())
				.status(UserNotificationStatus.PENDING)
				.type(UserNotificationType.INVITATION)
				.build()
		).collect(Collectors.toList());
		return StreamSupport.stream(userNotificationRepository.saveAll(invitations).spliterator(), false)
				.collect(Collectors.toList());
	}

	/**
	 * Sends a request on behalf of a user to join a community.
	 * @param user - user to send a request on behalf of
	 * @param community - community to join
	 * @return user notification
	 */
	@Transactional
	public UserNotification sendUserRequestToJoinCommunity(User user, Community community) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null.");
		}
		if (community == null) {
			throw new IllegalArgumentException("Community cannot be null.");
		}
		UserNotification userNotification = UserNotification.builder()
				.community(community)
				.initiator(user)
				.status(UserNotificationStatus.PENDING)
				.type(UserNotificationType.COMMUNITY_JOIN_REQUEST)
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.build();
		return userNotificationRepository.save(userNotification);
	}

}
