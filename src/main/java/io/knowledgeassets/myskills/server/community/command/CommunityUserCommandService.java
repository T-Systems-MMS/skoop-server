package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.exception.CommunityAccessDeniedException;
import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
public class CommunityUserCommandService {

	private final CommunityRepository communityRepository;
	private final UserQueryService userQueryService;

	public CommunityUserCommandService(CommunityRepository communityRepository,
								   UserQueryService userQueryService) {
		this.communityRepository = communityRepository;
		this.userQueryService = userQueryService;
	}

	/**
	 * User joins the community as member.
	 * @param communityId - id of the community to join
	 * @param userId - user id
	 */
	@Transactional
	public Community joinCommunityAsMember(String communityId, String userId) {
		final User user = userQueryService.getUserById(userId)
				.orElseThrow(buildNoSuchResourceExceptionSupplier("id", userId, Model.USER));
		final Community community = communityRepository.findById(communityId).orElseThrow(buildNoSuchResourceExceptionSupplier("communityId", communityId, Model.COMMUNITY));
		if (CommunityType.OPENED.equals(community.getType())) {
			final List<User> members;
			if (community.getMembers() != null) {
				members = new ArrayList<>(community.getMembers());
			} else {
				members = new ArrayList<>();
			}
			members.add(user);
			community.setMembers(members);
			community.setLastModifiedDate(LocalDateTime.now());
			return communityRepository.save(community);
		}
		else {
			throw new CommunityAccessDeniedException(format("The community with ID \"%s\" is not an open one." +
					" Only open communities can be joined without request.", communityId));
		}
	}

	private Supplier<NoSuchResourceException> buildNoSuchResourceExceptionSupplier(String paramName, String paramValue, Model community) {
		return () -> {
			final String[] searchParamsMap = {paramName, paramValue};
			return NoSuchResourceException.builder()
					.model(community)
					.searchParamsMap(searchParamsMap)
					.build();
		};
	}

	/**
	 * Authenticated user leaves the community.
	 * @param communityId - id of the community to leave
	 * @param userId - user id
	 */
	@Transactional
	public Community leaveCommunity(String communityId, String userId) {
		final User user = userQueryService.getUserById(userId)
				.orElseThrow(buildNoSuchResourceExceptionSupplier("id", userId, Model.USER));
		final Community community = communityRepository.findById(communityId).orElseThrow(buildNoSuchResourceExceptionSupplier("communityId", communityId, Model.COMMUNITY));

		if (community.getMembers().stream().noneMatch((User u) -> user.getId().equals(u.getId()))) {
			throw new InvalidInputException(format("The user \"%s\" is not a member of the community \"%s\"", user.getId(), community.getId()));
		}

		final List<User> newMemberList = community.getMembers().stream().filter((User u) -> !user.getId().equals(u.getId()))
				.collect(toList());
		community.setMembers(newMemberList);
		community.setLastModifiedDate(LocalDateTime.now());
		return communityRepository.save(community);
	}

}
