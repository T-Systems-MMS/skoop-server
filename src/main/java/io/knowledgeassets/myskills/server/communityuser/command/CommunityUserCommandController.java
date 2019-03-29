package io.knowledgeassets.myskills.server.communityuser.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserResponse;
import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.command.CommunityUserRegistrationApprovalCommand;
import io.knowledgeassets.myskills.server.communityuser.registration.command.CommunityUserRegistrationCommandService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = { "CommunityUsers" })
@RestController
@Validated
public class CommunityUserCommandController {

	private final CommunityUserCommandService communityUserCommandService;
	private final SecurityService securityService;
	private final CommunityQueryService communityQueryService;
	private final UserQueryService userQueryService;
	private final CommunityUserRegistrationQueryService communityUserRegistrationQueryService;
	private final CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	public CommunityUserCommandController(CommunityUserCommandService communityUserCommandService,
										  SecurityService securityService,
										  CommunityQueryService communityQueryService,
										  UserQueryService userQueryService,
										  CommunityUserRegistrationQueryService communityUserRegistrationQueryService,
										  CommunityUserRegistrationCommandService communityUserRegistrationCommandService) {
		this.communityUserCommandService = communityUserCommandService;
		this.securityService = securityService;
		this.communityQueryService = communityQueryService;
		this.userQueryService = userQueryService;
		this.communityUserRegistrationQueryService = communityUserRegistrationQueryService;
		this.communityUserRegistrationCommandService = communityUserRegistrationCommandService;
	}

	@ApiOperation(value = "Adds a user as a member to the community.",
			notes = "Adds a user as a member to the community. User ID is given in the request body.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PostMapping(path = "/communities/{communityId}/users")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CommunityUserResponse> addUserAsMemberToCommunity(@PathVariable("communityId") String communityId,
															   @RequestBody CommunityUserRequest request) {
		final Community community = communityQueryService.getCommunityById(communityId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", communityId};
			return NoSuchResourceException.builder()
					.model(Model.COMMUNITY)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final User user = userQueryService.getUserById(request.getUserId()).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", request.getUserId()};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Optional<CommunityUserRegistration> communityUserRegistration = communityUserRegistrationQueryService.getPendingUserRequestToJoinCommunity(request.getUserId(), communityId);
		if (securityService.isAuthenticatedUserId(request.getUserId()) && CommunityType.OPEN.equals(community.getType())) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(CommunityUserResponse.of(communityUserCommandService.create(community, user, CommunityRole.MEMBER)));
		} else if (securityService.isCommunityManager(communityId) && CommunityType.CLOSED.equals(community.getType())) {
			if (communityUserRegistration.isPresent()) {
				final CommunityUserRegistration registration = communityUserRegistrationCommandService.approve(communityUserRegistration.get(), CommunityUserRegistrationApprovalCommand.builder()
						.approvedByCommunity(true)
						.approvedByUser(null)
						.build()
				);
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(CommunityUserResponse.of(registration.getCommunityUser()));
			} else {
				throw new UserCommunityException(String.format("There is no pending community user registration for user '%s' and community '%s'", user.getUserName(), community.getTitle()));
			}
		} else {
			throw new UserCommunityException("The endpoint can be used either by a community manager to invite users to join a community or by authenticated user to join an open community.");
		}
	}

	@ApiOperation(value = "Change community role of the user.",
			notes = "Change community role of the user. Only managers of the community are allowed to call the endpoint.")
	@ApiResponses({
			@ApiResponse(code = 501, message = "Not implemented at the moment. Used for the future design."),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PutMapping(path = "/communities/{communityId}/users/{userId}")
	@PreAuthorize("isAuthenticated() and !isPrincipalUserId(#userId)")
	public ResponseEntity<CommunityUserResponse> changeCommunityUserRole(@PathVariable("communityId") String communityId,
																 @PathVariable("userId") String userId,
																 @RequestBody CommunityUserUpdateRequest request) {
		if (!securityService.isCommunityManager(communityId)) {
			throw new UserCommunityException("The user has to be a community manager to alter other user's membership.");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(CommunityUserResponse.of(communityUserCommandService.update(communityId, userId, request.getRole())));
		}
	}

	@ApiOperation(value = "User leaves the community.",
			notes = "User leaves the community.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@DeleteMapping(path = "/communities/{communityId}/users/{userId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> removeUserFromCommunity(
			@PathVariable("communityId") String communityId,
			@PathVariable("userId") String userId) {
		if ((!securityService.isCommunityManager(communityId) && !securityService.isAuthenticatedUserId(userId)) ||
				(securityService.isCommunityManager(communityId) && securityService.isAuthenticatedUserId(userId))) {
			throw new UserCommunityException("The authenticated user must be either a community manager or the one who is leaving the community. " +
					"And if authenticated user is a community manager she / he cannot remove herself / himself from the community.");
		} else {
			if (securityService.isCommunityManager(communityId)) {
				communityUserCommandService.kickoutUser(communityId, userId);
			} else {
				communityUserCommandService.leaveCommunity(communityId, userId);
			}
			return ResponseEntity.noContent().build();
		}
	}

}
