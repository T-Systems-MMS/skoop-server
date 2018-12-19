package io.knowledgeassets.myskills.server.skillgroup.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
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

@Api(tags = "SkillGroups", description = "API allowing queries of skill group suggestions for skill")
@RestController
public class SkillGroupSuggestionController {
	private SkillGroupQueryService skillGroupQueryService;

	public SkillGroupSuggestionController(SkillGroupQueryService skillGroupQueryService) {
		this.skillGroupQueryService = skillGroupQueryService;
	}

	@ApiOperation(value = "Get skill group suggestions for a specific skill",
			notes = "Get skill group suggestions for the specific skill and the given search term. Returns only the names " +
					"of skill groups which contain the search term (case insensitive comparison) and are not related to " +
					"the skill yet. The list is sorted in alphabetical order.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/group-suggestions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSkillGroupSuggestions(@RequestParam("search") String search) throws BusinessException {
		try {
			return skillGroupQueryService.getSkillGroupSuggestions(search).map(SkillGroup::getName).collect(toList());
		} catch (BusinessException e) {
			e.setDebugMessage("An exception has occurred in getting skill group suggestions for a specific user!");
			e.setSuggestion("Check skillId isn't null or make sure that skill with this skillId already exists in DB!");
			throw e;
		}
	}
}
