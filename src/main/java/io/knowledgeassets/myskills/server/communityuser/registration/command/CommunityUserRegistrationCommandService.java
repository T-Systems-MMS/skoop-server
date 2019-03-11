package io.knowledgeassets.myskills.server.communityuser.registration.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.communityuser.command.CommunityUserCommandService;
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

	public CommunityUserRegistrationCommandService(CommunityUserRegistrationRepository communityUserRegistrationRepository,
												   CurrentUserService currentUserService,
												   CommunityUserCommandService communityUserCommandService) {
		this.communityUserRegistrationRepository = communityUserRegistrationRepository;
		this.currentUserService = currentUserService;
		this.communityUserCommandService = communityUserCommandService;
	}

	@Transactional
	public CommunityUserRegistration approve(CommunityUserRegistration registration, CommunityUserRegistrationApprovalCommand command) {
		if (command.getApprovedByCommunity() != null) {
			registration.setApprovedByCommunity(command.getApprovedByCommunity());
		}
		if (command.getApprovedByUser() != null) {
			registration.setApprovedByUser(command.getApprovedByUser());
		}
		final CommunityUserRegistration communityUserRegistration = communityUserRegistrationRepository.save(registration);
		if (communityUserRegistration.getApprovedByCommunity() && communityUserRegistration.getApprovedByUser()) {
			communityUserRegistration.setCommunityUser(communityUserCommandService.create(registration.getCommunity(), registration.getRegisteredUser(), CommunityRole.MEMBER));
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
				.approvedByUser(false)
				.registeredUser(user)
				.community(community)
				.creationDatetime(now)
				.build()
		).collect(Collectors.toList());
		return StreamSupport.stream(communityUserRegistrationRepository.saveAll(communityUserRegistrations).spliterator(), false)
				.collect(Collectors.toList());
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
				.approvedByCommunity(false)
				.registeredUser(currentUserService.getCurrentUser())
				.id(UUID.randomUUID().toString())
				.creationDatetime(LocalDateTime.now())
				.build();
		return communityUserRegistrationRepository.save(communityUserRegistration);
	}

}
