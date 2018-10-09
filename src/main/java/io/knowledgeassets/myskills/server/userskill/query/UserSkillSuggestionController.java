package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.skill.Skill;
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

@Api(tags = "UserSkills", description = "API allowing queries of skill suggestions for users")
@RestController
public class UserSkillSuggestionController {
	private UserSkillQueryService userSkillQueryService;

	public UserSkillSuggestionController(UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
	}

	@ApiOperation(value = "Get skill suggestions for a specific user",
			notes = "Get skill suggestions for the specific user and the given search term. Returns only the names " +
					"of skills which contain the search term (case insensitive comparison) and are not related to " +
					"the user yet. The list is sorted in alphabetical order.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/users/{userId}/skill-suggestions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getUserSkillSuggestions(@PathVariable("userId") String userId,
												@RequestParam("search") String search) throws BusinessException {
		try {
			return userSkillQueryService.getUserSkillSuggestions(userId, search).map(Skill::getName).collect(toList());
		} catch (BusinessException e) {
			e.setDebugMessage("An exception has occurred in getting skill suggestions for a specific user!");
			e.setSuggestion("Check userId isn't null or make sure that user with this userId already existsReport in DB!");
			throw e;
		}
	}
}
