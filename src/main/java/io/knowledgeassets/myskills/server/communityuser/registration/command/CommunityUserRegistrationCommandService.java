package io.knowledgeassets.myskills.server.communityuser.registration.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.communityuser.command.CommunityUserCommandService;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationType;
import io.knowledgeassets.myskills.server.notification.command.NotificationCommandService;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommunityUserRegistrationCommandService {

	private final CommunityUserRegistrationRepository communityUserRegistrationRepository;
	private final CurrentUserService currentUserService;
	private final CommunityUserCommandService communityUserCommandService;
	private final NotificationCommandService notificationCommandService;

	public CommunityUserRegistrationCommandService(CommunityUserRegistrationRepository communityUserRegistrationRepository,
												   CurrentUserService currentUserService,
												   CommunityUserCommandService communityUserCommandService,
												   NotificationCommandService notificationCommandService) {
		this.communityUserRegistrationRepository = communityUserRegistrationRepository;
		this.currentUserService = currentUserService;
		this.communityUserCommandService = communityUserCommandService;
		this.notificationCommandService = notificationCommandService;
	}

	@Transactional
	public CommunityUserRegistration approve(CommunityUserRegistration registration, CommunityUserRegistrationApprovalCommand command) {
		if (command.getApprovedByUser() != null && command.getApprovedByCommunity() != null) {
			throw new IllegalArgumentException("No one can approve / decline both as community and user at the same time.");
		}
		if (command.getApprovedByUser() == null && command.getApprovedByCommunity() == null) {
			throw new IllegalArgumentException("The command to approve user registration will not affect user registration as it have both flags set to null.");
		}
		if (command.getApprovedByCommunity() != null) {
			registration.setApprovedByCommunity(command.getApprovedByCommunity());
		}
		if (command.getApprovedByUser() != null) {
			registration.setApprovedByUser(command.getApprovedByUser());
		}
		final CommunityUserRegistration communityUserRegistration = communityUserRegistrationRepository.save(registration);
		if (Boolean.TRUE.equals(communityUserRegistration.getApprovedByCommunity()) &&
				Boolean.TRUE.equals(communityUserRegistration.getApprovedByUser())) {
			communityUserRegistration.setCommunityUser(communityUserCommandService.create(registration.getCommunity(), registration.getRegisteredUser(), CommunityRole.MEMBER));
		}
		if (Boolean.TRUE.equals(communityUserRegistration.getApprovedByCommunity()) &&
				Boolean.TRUE.equals(communityUserRegistration.getApprovedByUser()) &&
				Boolean.TRUE.equals(command.getApprovedByCommunity())) {
			notificationCommandService.save(Notification.builder()
					.id(UUID.randomUUID().toString())
					.type(NotificationType.ACCEPTANCE_TO_COMMUNITY)
					.creationDatetime(LocalDateTime.now())
					.registration(communityUserRegistration)
					.build()
			);
		}
		return communityUserRegistration;
	}

	/**
	 * Invite users to join a community.
	 * @param users - invited users
	 * @param community - community to join
	 * @return user invitations
	 */
	@Transactional
	public List<CommunityUserRegistration> createUserRegistrationsOnBehalfOfCommunity(List<User> users, Community community) {
		if (CollectionUtils.isEmpty(users)) {
			throw new IllegalArgumentException("There are no users no invite.");
		}
		final LocalDateTime now = LocalDateTime.now();
		final List<CommunityUserRegistration> communityUserRegistrations = users.stream().map(user -> CommunityUserRegistration.builder()
				.id(UUID.randomUUID().toString())
				.approvedByCommunity(true)
				.approvedByUser(null)
				.registeredUser(user)
				.community(community)
				.creationDatetime(now)
				.build()
		).collect(Collectors.toList());
		List<CommunityUserRegistration> registrations = StreamSupport.stream(communityUserRegistrationRepository.saveAll(communityUserRegistrations).spliterator(), false)
				.collect(Collectors.toList());
		registrations.stream().map(registration -> Notification.builder()
				.id(UUID.randomUUID().toString())
				.registration(registration)
				.creationDatetime(LocalDateTime.now())
				.type(NotificationType.INVITATION_TO_JOIN_COMMUNITY)
				.build()
		).forEach(notificationCommandService::save);
		return registrations;
	}

	/**
	 * Sends a request on behalf of a user to join a community.
	 * @param user - user to send a request on behalf of
	 * @param community - community to join
	 * @return user notification
	 */
	@Transactional
	public CommunityUserRegistration createUserRegistrationsOnBehalfOfUser(User user, Community community) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null.");
		}
		if (community == null) {
			throw new IllegalArgumentException("Community cannot be null.");
		}
		CommunityUserRegistration communityUserRegistration = CommunityUserRegistration.builder()
				.community(community)
				.approvedByUser(true)
				.approvedByCommunity(null)
				.registeredUser(currentUserService.getCurrentUser())
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.build();
		final CommunityUserRegistration registration = communityUserRegistrationRepository.save(communityUserRegistration);
		notificationCommandService.save(Notification.builder()
				.id(UUID.randomUUID().toString())
				.registration(registration)
				.creationDatetime(LocalDateTime.now())
				.type(NotificationType.REQUEST_TO_JOIN_COMMUNITY)
				.build()
		);
		return registration;
	}

}
