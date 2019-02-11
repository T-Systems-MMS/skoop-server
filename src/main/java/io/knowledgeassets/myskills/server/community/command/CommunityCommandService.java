package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.exception.CommunityAccessDeniedException;
import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.lang.String.format;

@Service
public class CommunityCommandService {

	private final CommunityRepository communityRepository;
	private final CurrentUserService currentUserService;

	public CommunityCommandService(CommunityRepository communityRepository,
								   CurrentUserService currentUserService) {
		this.communityRepository = communityRepository;
		this.currentUserService = currentUserService;
	}

	@Transactional
	public Community create(Community community) {
		communityRepository.findByTitleIgnoreCase(community.getTitle()).ifPresent(skill -> {
			throw DuplicateResourceException.builder()
					.message(format("Community with title '%s' already exists", community.getTitle()))
					.build();
		});
		final LocalDateTime now = LocalDateTime.now();
		community.setManagers(singletonList(currentUserService.getCurrentUser()));
		community.setMembers(singletonList(currentUserService.getCurrentUser()));
		community.setCreationDate(now);
		community.setLastModifiedDate(now);
		community.setId(UUID.randomUUID().toString());
		return communityRepository.save(community);
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
		community.setLastModifiedDate(LocalDateTime.now());
		community.setCreationDate(p.getCreationDate());
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
		communityRepository.delete(community);
	}


	/**
	 * Authenticated user joins the community as member.
	 * @param communityId - id of the community to join
	 */
	@Transactional
	public Community joinCommunityAsMember(String communityId) {
		final User user = currentUserService.getCurrentUser();
		final Community community = communityRepository.findById(communityId).orElseThrow(() -> {
			final String[] searchParamsMap = {"communityId", communityId};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		if (CommunityType.OPENED.equals(community.getType())) {
			final List<User> members;
			if (community.getMembers() != null) {
				members = community.getMembers();
			} else {
				members = new ArrayList<>();
			}
			members.add(user);
			community.setMembers(members);
			return update(community);
		}
		else {
			throw new CommunityAccessDeniedException(format("The community with ID \"%s\" is not an open one." +
					" Only open communities can be joined without request.", communityId));
		}
	}

}
