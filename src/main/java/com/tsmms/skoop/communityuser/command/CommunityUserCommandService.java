package com.tsmms.skoop.communityuser.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.communityuser.CommunityUserRoleChangedNotification;
import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.notification.NotificationRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.CommunityUserRepository;
import com.tsmms.skoop.communityuser.UserKickedOutFromCommunityNotification;
import com.tsmms.skoop.communityuser.UserLeftCommunityNotification;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.notification.command.NotificationCommandService;
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
	private final NotificationCommandService notificationCommandService;

	public CommunityUserCommandService(CommunityUserRepository communityUserRepository,
									   NotificationRepository notificationRepository,
									   NotificationCommandService notificationCommandService) {
		this.communityUserRepository = requireNonNull(communityUserRepository);
		this.notificationRepository = requireNonNull(notificationRepository);
		this.notificationCommandService = requireNonNull(notificationCommandService);
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
		communityUserRepository.findByUserIdAndCommunityId(user.getId(), community.getId()).ifPresent(communityUser -> {
			throw DuplicateResourceException.builder()
					.message(format("The relationship between user '%s' and community '%s' already exists", community.getTitle(), user.getUserName()))
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
			final CommunityUser result = communityUserRepository.save(communityUser);
			notificationCommandService.save(CommunityUserRoleChangedNotification.builder()
					.id(UUID.randomUUID().toString())
					.communityName(communityUser.getCommunity().getTitle())
					.creationDatetime(LocalDateTime.now())
					.role(result.getRole())
					.user(result.getUser())
					.build()
			);
			return result;
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
				.communityName(communityUser.getCommunity().getTitle())
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
				.communityName(communityUser.getCommunity().getTitle())
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
