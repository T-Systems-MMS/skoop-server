package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityResponse;
import io.knowledgeassets.myskills.server.security.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = { "CommunityUsers" })
@RestController
@Validated
public class CommunityUserCommandController {

	private final CommunityUserCommandService communityUserCommandService;
	private final SecurityService securityService;

	public CommunityUserCommandController(CommunityUserCommandService communityUserCommandService,
										  SecurityService securityService) {
		this.communityUserCommandService = communityUserCommandService;
		this.securityService = securityService;
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
	@PostMapping(path = "/communities/{communityId}/users")
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#request.userId)")
	public ResponseEntity<CommunityResponse> joinCommunity(@PathVariable("communityId") String communityId,
														   @RequestBody CommunityUserRequest request) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(CommunityResponse.of(communityUserCommandService.joinCommunityAsMember(communityId, request.getUserId())));
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
	@PreAuthorize("isAuthenticated() and hasCommunityManagerRole(#communityId) and !isPrincipalUserId(#userId)")
	public ResponseEntity<CommunityResponse> changeCommunityUserRole(@PathVariable("communityId") String communityId,
																	 @PathVariable("userId") String userId,
																	 @RequestBody CommunityUserUpdateRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
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
	@PreAuthorize("(isAuthenticated() and isPrincipalUserId(#userId)) or hasCommunityManagerRole(#communityId)")
	public ResponseEntity<CommunityResponse> leaveCommunity(
			@ApiIgnore @AuthenticationPrincipal Jwt jwt,
			@PathVariable("communityId") String communityId,
			@PathVariable("userId") String userId) {
		final Community community = communityUserCommandService.leaveCommunity(communityId, userId);
		final CommunityResponse communityResponse = securityService.hasCommunityManagerRole(jwt, communityId) ?
				CommunityResponse.of(community) : CommunityResponse.simple(community);
		return ResponseEntity.status(HttpStatus.OK)
				.body(communityResponse);
	}

}
