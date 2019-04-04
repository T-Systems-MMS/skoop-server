package com.tsmms.skoop.user.community;

import com.tsmms.skoop.community.CommunityResponse;
import com.tsmms.skoop.community.query.CommunityQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "UserCommunities")
@RestController
public class UserCommunityController {

	private final CommunityQueryService communityQueryService;

	public UserCommunityController(CommunityQueryService communityQueryService) {
		this.communityQueryService = communityQueryService;
	}

	@ApiOperation(value = "Get communities recommended for a user.",
			notes = "Get communities recommended for a user. " +
					"The recommended communities are communities which have associated skills user has priority at least at level 2.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/community-recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CommunityResponse> getRecommendedCommunities(@PathVariable("userId") String userId) {
		return communityQueryService.getCommunitiesRecommendedForUser(userId)
				.map(CommunityResponse::of)
				.collect(toList());
	}

	@ApiOperation(value = "Get communities the user takes part in.",
			notes = "Get communities the user takes part in.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/communities", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CommunityResponse> getUserCommunities(@PathVariable("userId") String userId) {
		return communityQueryService.getUserCommunities(userId)
				.map(CommunityResponse::of)
				.collect(toList());
	}

}
