package io.knowledgeassets.myskills.server.skillgroup.query;

import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroupResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "SkillGroups")
@RestController
public class SkillGroupQueryController {
	private SkillGroupQueryService skillGroupQueryService;

	public SkillGroupQueryController(SkillGroupQueryService skillGroupQueryService) {
		this.skillGroupQueryService = skillGroupQueryService;
	}

	@ApiOperation(value = "Get all skill groups",
			notes = "Get all skill groups currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	// TODO: Change API path to qualified name "/skill-groups"
	@GetMapping(path = "/groups", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SkillResponse> getGroups() {
		return skillGroupQueryService.getSkillGroups()
				.map(skillGroup -> SkillResponse.builder()
						.id(skillGroup.getId())
						.name(skillGroup.getName())
						.description(skillGroup.getDescription())
						.build())
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific skill group",
			notes = "Get a specific skill group currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	// TODO: Change API path to qualified name "/skill-groups"
	@GetMapping(path = "/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SkillGroupResponse getSkill(@PathVariable("groupId") String groupId) {
		return skillGroupQueryService.getSkillGroupById(groupId)
				.map(skillGroup -> SkillGroupResponse.builder()
						.id(skillGroup.getId())
						.name(skillGroup.getName())
						.description(skillGroup.getDescription())
						.build())
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", groupId};
					return NoSuchResourceException.builder()
							.model(Model.SKILL_GROUP)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

	@ApiOperation(value = "If a skill group with the specific name exists it return true, otherwise false.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/groups/group-existence", produces = MediaType.APPLICATION_JSON_VALUE)
	// TODO: Replace this method by adding a filter parameter to the "GET /groups" endpoint to search for group name.
	public Boolean isGroupExist(@RequestParam("search") String skillName) {
		return skillGroupQueryService.isSkillGroupExist(skillName);
	}
}
