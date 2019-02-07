package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRequest;
import io.knowledgeassets.myskills.server.community.CommunityResponse;
import io.knowledgeassets.myskills.server.community.Link;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Communities", description = "API allowing modifications of communities")
@RestController
public class CommunityCommandController {

	private final CommunityCommandService communityCommandService;

	public CommunityCommandController(CommunityCommandService communityCommandService) {
		this.communityCommandService = communityCommandService;
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
		final Community result = communityCommandService.create(community);
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
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/communities/{communityId}")
	public ResponseEntity<Void> delete(@PathVariable("communityId") String communityId) {
		communityCommandService.delete(communityId);
		return ResponseEntity.noContent().build();
	}

	private Community convertCommunityRequestToCommunityDomain(CommunityRequest communityRequest) {
		return Community.builder()
				.title(communityRequest.getTitle())
				.description(communityRequest.getDescription())
				.links(convertLinkRequestListToLinkList(communityRequest))
				.build();
	}

	private List<Link> convertLinkRequestListToLinkList(CommunityRequest communityRequest) {
		if (communityRequest.getLinks() == null) {
			return null;
		}
		return communityRequest.getLinks().stream().map(linkRequest -> Link.builder()
				.name(linkRequest.getName())
				.href(linkRequest.getHref())
				.build()
		).collect(Collectors.toList());
	}

}