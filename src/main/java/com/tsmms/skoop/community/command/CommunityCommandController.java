package com.tsmms.skoop.community.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.UserCommunityException;
import com.tsmms.skoop.community.CommunityRequest;
import com.tsmms.skoop.community.CommunityResponse;
import com.tsmms.skoop.community.Link;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.security.SecurityService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Api(tags = { "Communities" })
@RestController
@Validated
public class CommunityCommandController {

	private final CommunityCommandService communityCommandService;
	private final UserQueryService userQueryService;
	private final SkillQueryService skillQueryService;
	private final SecurityService securityService;

	public CommunityCommandController(CommunityCommandService communityCommandService,
									  UserQueryService userQueryService,
									  SkillQueryService skillQueryService,
									  SecurityService securityService) {
		this.communityCommandService = communityCommandService;
		this.userQueryService = userQueryService;
		this.skillQueryService = skillQueryService;
		this.securityService = securityService;
	}

	@ApiOperation(value = "Create new community",
			notes = "Create new community in the system. The community name must not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@PostMapping(path = "/communities",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommunityResponse> create(@RequestBody @Valid CommunityRequest request) {
		final Community community = convertCommunityRequestToCommunityDomain(request);
		final Community result = communityCommandService.create(community, getInvitedUsers(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(CommunityResponse.of(result));
	}

	@ApiOperation(value = "Update an existing community",
			notes = "Update an existing community in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@PutMapping(path = "/communities/{communityId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommunityResponse> update(@PathVariable("communityId") String communityId, @Valid @RequestBody CommunityRequest request) {
		if (!securityService.isCommunityManager(communityId)) {
			throw new UserCommunityException("The user has to be a community manager to alter the community.");
		}
		final Community community = convertCommunityRequestToCommunityDomain(request);
		community.setId(communityId);
		final Community result = communityCommandService.update(community);
		return ResponseEntity.status(HttpStatus.OK).body(CommunityResponse.of(result));
	}

	@ApiOperation(value = "Delete an existing community",
			notes = "Delete an existing community from the system. All relationships will be discarded!")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(path = "/communities/{communityId}")
	public ResponseEntity<Void> delete(@PathVariable("communityId") String communityId) {
		if (!securityService.isCommunityManager(communityId)) {
			throw new UserCommunityException("The user has to be a community manager to delete the community.");
		}
		communityCommandService.delete(communityId);
		return ResponseEntity.noContent().build();
	}

	private List<User> getInvitedUsers(CommunityRequest communityRequest) {
		if (communityRequest.getInvitedUserIds() == null) {
			return Collections.emptyList();
		} else {
			return communityRequest.getInvitedUserIds().stream().map(invitedUserId -> userQueryService.getUserById(invitedUserId).orElseThrow(() -> {
				String[] searchParamsMap = {"id", invitedUserId};
				return NoSuchResourceException.builder()
						.model(Model.USER)
						.searchParamsMap(searchParamsMap)
						.build();
			})).collect(toList());
		}
	}

	private Community convertCommunityRequestToCommunityDomain(CommunityRequest communityRequest) {
		final List<Skill> skills;
		if (communityRequest.getSkillNames() != null) {
			skills = communityRequest.getSkillNames().stream().map(skillName ->
					skillQueryService.findByNameIgnoreCase(skillName).orElse(
							Skill.builder()
									.name(skillName)
									.build()
					)).collect(toList());
		}
		else {
			skills = Collections.emptyList();
		}
		return Community.builder()
				.title(communityRequest.getTitle())
				.type(communityRequest.getType())
				.description(communityRequest.getDescription())
				.links(convertLinkRequestListToLinkList(communityRequest))
				.skills(skills)
				.build();
	}

	private List<Link> convertLinkRequestListToLinkList(CommunityRequest communityRequest) {
		if (communityRequest.getLinks() == null) {
			return Collections.emptyList();
		}
		return communityRequest.getLinks().stream().map(linkRequest -> Link.builder()
				.name(linkRequest.getName())
				.href(linkRequest.getHref())
				.build()
		).collect(Collectors.toList());
	}

}
