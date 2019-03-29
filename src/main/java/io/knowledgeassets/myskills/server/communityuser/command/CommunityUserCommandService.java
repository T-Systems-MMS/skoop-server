package io.knowledgeassets.myskills.server.communityuser.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserRepository;
import io.knowledgeassets.myskills.server.communityuser.notification.kickout.UserKickedOutFromCommunityNotification;
import io.knowledgeassets.myskills.server.communityuser.notification.leaving.UserLeftCommunityNotification;
import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.notification.NotificationRepository;
import io.knowledgeassets.myskills.server.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
public class CommunityUserCommandService {

	private final CommunityUserRepository communityUserRepository;
	private final NotificationRepository notificationRepository;

	public CommunityUserCommandService(CommunityUserRepository communityUserRepository,
									   NotificationRepository notificationRepository) {
		this.communityUserRepository = requireNonNull(communityUserRepository);
		this.notificationRepository = requireNonNull(notificationRepository);
	}

	/**
	 * User joins the community as member.
	 * @param community - community to associate user with
	 * @param user - user
	 * @param role - role of the user
	 * @return community user
	 */
	@Transactional
	public CommunityUser create(Community community, User user, CommunityRole role) {
		checkArguments(community, user, role);
		final LocalDateTime now = LocalDateTime.now();
		communityUserRepository.findByUserIdAndCommunityIdAndRole(user.getId(), community.getId(), role).ifPresent(communityUser -> {
			throw DuplicateResourceException.builder()
					.message(format("The relationship between user '%s' and community '%s' with the role '%s' already exists", community.getTitle(), user.getUserName(), role.toString()))
					.build();
		});
		return communityUserRepository.save(CommunityUser.builder()
				.community(community)
				.user(user)
				.role(role)
				.creationDate(now)
				.lastModifiedDate(now)
				.build());
	}

	private void checkArguments(Community community, User user, CommunityRole role) {
		if (community == null) {
			throw new IllegalArgumentException("Community cannot be null");
		}
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if (role == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}
	}

	/**
	 * Updates role of the specified user in the specified community.
	 * @param community - the community user's role has to be changed in
	 * @param user - the user whose role has to be changed
	 * @param role - new role of the user
	 * @return community user
	 */
	@Transactional
	public CommunityUser update(Community community, User user, CommunityRole role) {
		checkArguments(community, user, role);
		return update(community.getId(), user.getId(), role);
	}

	/**
	 * Updates role of a user with the specified ID in a community with the specified ID.
	 * @param communityId - ID of the community user's role has to be changed in
	 * @param userId - ID of the user whose role has to be changed
	 * @param role - new role of the user
	 * @return community user
	 */
	@Transactional
	public CommunityUser update(String communityId, String userId, CommunityRole role) {
		if (communityId == null) {
			throw new IllegalArgumentException("Community ID cannot be null");
		}
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null");
		}
		if (role == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}
		final CommunityUser communityUser = communityUserRepository.findByUserIdAndCommunityId(userId, communityId).orElseThrow(() -> {
			String[] searchParamsMap = {"communityId", communityId, "userId", userId};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY_USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		if (communityUser.getRole().equals(role)) {
			return communityUser;
		} else {
			communityUser.setRole(role);
			communityUser.setLastModifiedDate(LocalDateTime.now());
			return communityUserRepository.save(communityUser);
		}
	}

	/**
	 * Kicks out the user from the community.
	 * @param communityId community ID
	 * @param userId user ID
	 */
	@Transactional
	public void kickoutUser(String communityId, String userId) {
		final CommunityUser communityUser = remove(communityId, userId);
		notificationRepository.save(UserKickedOutFromCommunityNotification.builder()
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.community(communityUser.getCommunity())
				.user(communityUser.getUser())
				.build()
		);
	}

	/**
	 * The user leaves the community.
	 * @param communityId community ID
	 * @param userId user ID
	 */
	@Transactional
	public void leaveCommunity(String communityId, String userId) {
		final CommunityUser communityUser = remove(communityId, userId);
		notificationRepository.save(UserLeftCommunityNotification.builder()
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.community(communityUser.getCommunity())
				.user(communityUser.getUser())
				.build()
		);
	}

	private CommunityUser remove(String communityId, String userId) {
		if (communityId == null) {
			throw new IllegalArgumentException("Community ID cannot be null");
		}
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		final CommunityUser communityUser = communityUserRepository.findByUserIdAndCommunityId(userId, communityId).orElseThrow(() -> {
			String[] searchParamsMap = {"communityId", communityId, "userId", userId};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		communityUserRepository.delete(communityUser);
		return communityUser;
	}

}
