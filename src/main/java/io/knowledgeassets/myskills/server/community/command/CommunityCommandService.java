package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityDeletedNotification;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.command.CommunityUserCommandService;
import io.knowledgeassets.myskills.server.communityuser.query.CommunityUserQueryService;
import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.notification.command.NotificationCommandService;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.communityuser.registration.command.CommunityUserRegistrationCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.Objects.requireNonNull;

@Service
public class CommunityCommandService {

	private final CommunityRepository communityRepository;
	private final CurrentUserService currentUserService;
	private final SkillCommandService skillCommandService;
	private final CommunityUserRegistrationCommandService communityUserRegistrationCommandService;
	private final CommunityUserCommandService communityUserCommandService;
	private final CommunityUserQueryService communityUserQueryService;
	private final NotificationCommandService notificationCommandService;

	public CommunityCommandService(CommunityRepository communityRepository,
								   CurrentUserService currentUserService,
								   SkillCommandService skillCommandService,
								   CommunityUserRegistrationCommandService communityUserRegistrationCommandService,
								   CommunityUserCommandService communityUserCommandService,
								   CommunityUserQueryService communityUserQueryService,
								   NotificationCommandService notificationCommandService) {
		this.communityRepository = requireNonNull(communityRepository);
		this.currentUserService = requireNonNull(currentUserService);
		this.skillCommandService = requireNonNull(skillCommandService);
		this.communityUserRegistrationCommandService = requireNonNull(communityUserRegistrationCommandService);
		this.communityUserCommandService = requireNonNull(communityUserCommandService);
		this.communityUserQueryService = requireNonNull(communityUserQueryService);
		this.notificationCommandService = requireNonNull(notificationCommandService);
	}

	@Transactional
	public Community create(Community community, List<User> invitedUsers) {
		communityRepository.findByTitleIgnoreCase(community.getTitle()).ifPresent(skill -> {
			throw DuplicateResourceException.builder()
					.message(format("Community with title '%s' already exists", community.getTitle()))
					.build();
		});
		final LocalDateTime now = LocalDateTime.now();
		community.setCreationDate(now);
		community.setLastModifiedDate(now);
		community.setId(UUID.randomUUID().toString());
		community.setSkills(createNonExistentSkills(community));
		final Community c = communityRepository.save(community);
		communityUserCommandService.create(c, currentUserService.getCurrentUser(), CommunityRole.MANAGER);
		if (!CollectionUtils.isEmpty(invitedUsers)) {
			communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(invitedUsers, c);
		}
		return c;
	}

	@Transactional
	public Community update(Community community) {
		final Community p = communityRepository.findById(community.getId()).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", community.getId()};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		community.setCreationDate(p.getCreationDate());
		community.setLastModifiedDate(LocalDateTime.now());
		community.setCommunityUsers(p.getCommunityUsers());
		community.setSkills(createNonExistentSkills(community));
		return communityRepository.save(community);
	}

	@Transactional
	public void delete(String id) {
		final Community community = communityRepository.findById(id).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		Stream<CommunityUser> communityMembers = communityUserQueryService.getCommunityUsers(id, null);
		communityRepository.delete(community);
		notificationCommandService.save(CommunityDeletedNotification.builder()
				.id(UUID.randomUUID().toString())
				.communityName(community.getTitle())
				.creationDatetime(LocalDateTime.now())
				.recipients(communityMembers.map(CommunityUser::getUser).collect(toList()))
				.build()
		);
	}

	private List<Skill> createNonExistentSkills(Community community) {
		if (community.getSkills() != null) {
			return community.getSkills().stream().map(skill -> {
				if (skill.getId() == null) {
					return skillCommandService.createSkill(skill.getName(), null, null);
				} else {
					return skill;
				}
			}).collect(toList());
		}
		else {
			return Collections.emptyList();
		}
	}

}
