package io.knowledgeassets.myskills.server.community.query;

import io.knowledgeassets.myskills.server.community.CommunityResponse;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
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

@Api(tags = "Communities", description = "API allowing queries of communities")
@RestController
public class CommunityQueryController {

	private final CommunityQueryService communityQueryService;

	public CommunityQueryController(CommunityQueryService communityQueryService) {
		this.communityQueryService = communityQueryService;
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
		return communityQueryService.getCommunities()
				.map(CommunityResponse::of)
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
		return communityQueryService.getCommunityById(communityId)
				.map(CommunityResponse::of)
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", communityId};
					return NoSuchResourceException.builder()
							.model(Model.COMMUNITY)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

}
