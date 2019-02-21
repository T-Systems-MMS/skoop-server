package io.knowledgeassets.myskills.server.community.query;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityResponse;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Api(tags = "Communities")
@RestController
public class CommunityQueryController {

	private final CommunityQueryService communityQueryService;
	private final CurrentUserService currentUserService;

	public CommunityQueryController(CommunityQueryService communityQueryService,
									CurrentUserService currentUserService) {
		this.communityQueryService = communityQueryService;
		this.currentUserService = currentUserService;
	}

	@ApiOperation(value = "Get all communities",
			notes = "Get all communities currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/communities", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CommunityResponse> getCommunities() {
		final User currentUser = currentUserService.getCurrentUser();
		return communityQueryService.getCommunities()
				.map(convertCommunityToCommunityResponse(currentUser))
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific community",
			notes = "Get a specific community currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/communities/{communityId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommunityResponse getProject(@PathVariable("communityId") String communityId) {
		final User currentUser = currentUserService.getCurrentUser();
		return communityQueryService.getCommunityById(communityId)
				.map(convertCommunityToCommunityResponse(currentUser))
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", communityId};
					return NoSuchResourceException.builder()
							.model(Model.COMMUNITY)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

	private Function<Community, CommunityResponse> convertCommunityToCommunityResponse(User currentUser) {
		return (Community c) -> {
			if (c.getMembers() != null && c.getMembers().stream().map(User::getId).collect(Collectors.toSet()).contains(currentUser.getId())) {
				return CommunityResponse.of(c);
			}
			else {
				return CommunityResponse.simple(c);
			}
		};
	}

}
