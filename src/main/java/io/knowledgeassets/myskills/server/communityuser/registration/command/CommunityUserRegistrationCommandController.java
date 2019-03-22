package io.knowledgeassets.myskills.server.communityuser.registration.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.command.CommunityUserCommandService;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationResponse;
import io.knowledgeassets.myskills.server.communityuser.registration.query.CommunityUserRegistrationQueryService;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.UserCommunityException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.security.SecurityService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Api(tags = { "CommunityUserRegistrations" })
@RestController
public class CommunityUserRegistrationCommandController {

	private final CommunityUserRegistrationCommandService communityUserRegistrationCommandService;
	private final SecurityService securityService;
	private final UserQueryService userQueryService;
	private final CommunityQueryService communityQueryService;
	private final CommunityUserRegistrationQueryService communityUserRegistrationQueryService;
	private final CommunityUserCommandService communityUserCommandService;

	public CommunityUserRegistrationCommandController(CommunityUserRegistrationCommandService communityUserRegistrationCommandService,
													  SecurityService securityService,
													  UserQueryService userQueryService,
													  CommunityQueryService communityQueryService,
													  CommunityUserRegistrationQueryService communityUserRegistrationQueryService,
													  CommunityUserCommandService communityUserCommandService) {
		this.communityUserRegistrationCommandService = requireNonNull(communityUserRegistrationCommandService);
		this.securityService = requireNonNull(securityService);
		this.userQueryService = requireNonNull(userQueryService);
		this.communityQueryService = requireNonNull(communityQueryService);
		this.communityUserRegistrationQueryService = requireNonNull(communityUserRegistrationQueryService);
		this.communityUserCommandService = requireNonNull(communityUserCommandService);
	}

	@ApiOperation(value = "User joins the community as member.",
			notes = "User joins the community as member.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PostMapping(path = "/communities/{communityId}/user-registrations")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<CommunityUserRegistrationResponse>> createRequestToJoinCommunity(@PathVariable("communityId") String communityId,
																						  @RequestBody CommunityUserRegistrationRequest request) {
		final List<User> users = userQueryService.getUsersByIds(request.getUserIds()).collect(Collectors.toList());
		final Community community = communityQueryService.getCommunityById(communityId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", communityId};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final List<CommunityUserRegistrationResponse> result;
		if (securityService.isCommunityManager(communityId)) {
			result = communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(users, community).stream().map(CommunityUserRegistrationResponse::of).collect(Collectors.toList());
		} else if (CommunityType.OPEN.equals(community.getType()) && securityService.isCommunityMember(communityId)) {
			if (users.stream().anyMatch(user -> securityService.isAuthenticatedUserId(user.getId()))) {
				throw new UserCommunityException("The authenticated user cannot invite herself / himself to join a community.");
			}
			result = communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(users, community).stream().map(CommunityUserRegistrationResponse::of).collect(Collectors.toList());
		} else if (request.getUserIds().size() == 1 && securityService.isAuthenticatedUserId(request.getUserIds().get(0))) {
			if (CommunityType.OPEN.equals(community.getType())) {
				// user is added as member immediately because registrations for open communities can be regarded as "auto-approved by the community
				final CommunityUser communityUser = communityUserCommandService.create(community, users.get(0), CommunityRole.MEMBER);
				result = Collections.singletonList(
						CommunityUserRegistrationResponse.of(CommunityUserRegistration.builder()
								.registeredUser(communityUser.getUser())
								.approvedByCommunity(true)
								.approvedByUser(true)
								.build())
				);
			} else {
				result = Collections.singletonList(CommunityUserRegistrationResponse.of(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfUser(users.get(0), community)));
			}
		} else {
			throw new UserCommunityException();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@ApiOperation(value = "Update user registration.",
			notes = "Update user registration.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PutMapping(path = "/communities/{communityId}/user-registrations/{registrationId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CommunityUserRegistrationResponse> updateUserRegistration(@PathVariable("communityId") String communityId,
																					@PathVariable("registrationId") String registrationId,
																					@RequestBody CommunityUserRegistrationApprovalRequest request) {
		final CommunityUserRegistration registration = communityUserRegistrationQueryService.getCommunityUserRegistrationById(registrationId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", registrationId};
			return NoSuchResourceException.builder()
					.model(Model.USER_REGISTRATION)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final CommunityUserRegistrationApprovalCommand command = request.command();
		if (securityService.isCommunityManager(communityId)) {
			command.setApprovedByUser(null);
			return ResponseEntity.ok(CommunityUserRegistrationResponse.of(communityUserRegistrationCommandService.approve(registration, command)));
		} else if (securityService.isAuthenticatedUserId(registration.getRegisteredUser().getId())) {
			command.setApprovedByCommunity(null);
			return ResponseEntity.ok(CommunityUserRegistrationResponse.of(communityUserRegistrationCommandService.approve(registration, command)));
		} else {
			throw new UserCommunityException("The user is not authorized to update the registration. " +
					"Only community managers of a target community or a registered user can approve the registration.");
		}
	}

}
