package com.tsmms.skoop.community.command;

import com.tsmms.skoop.community.CommunityDeletedNotification;
import com.tsmms.skoop.community.link.command.LinkCommandService;
import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.command.CommunityUserCommandService;
import com.tsmms.skoop.communityuser.query.CommunityUserQueryService;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.registration.command.CommunityUserRegistrationCommandService;
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
	private final LinkCommandService linkCommandService;

	public CommunityCommandService(CommunityRepository communityRepository,
								   CurrentUserService currentUserService,
								   SkillCommandService skillCommandService,
								   CommunityUserRegistrationCommandService communityUserRegistrationCommandService,
								   CommunityUserCommandService communityUserCommandService,
								   CommunityUserQueryService communityUserQueryService,
								   NotificationCommandService notificationCommandService,
								   LinkCommandService linkCommandService) {
		this.communityRepository = requireNonNull(communityRepository);
		this.currentUserService = requireNonNull(currentUserService);
		this.skillCommandService = requireNonNull(skillCommandService);
		this.communityUserRegistrationCommandService = requireNonNull(communityUserRegistrationCommandService);
		this.communityUserCommandService = requireNonNull(communityUserCommandService);
		this.communityUserQueryService = requireNonNull(communityUserQueryService);
		this.notificationCommandService = requireNonNull(notificationCommandService);
		this.linkCommandService = requireNonNull(linkCommandService);
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
		p.setLastModifiedDate(LocalDateTime.now());
		p.setType(community.getType());
		p.setTitle(community.getTitle());
		p.setDescription(community.getDescription());
		p.setSkills(createNonExistentSkills(community));
		if (p.getLinks() != null) {
			p.getLinks().forEach(link -> {
				if (!community.getLinks().contains(link)) {
					linkCommandService.delete(link);
				}
			});
		}
		p.setLinks(community.getLinks());
		return communityRepository.save(p);
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
