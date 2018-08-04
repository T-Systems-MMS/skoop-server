package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.user.UserResponse;
import io.knowledgeassets.myskills.server.userskill.SkillUserResponse;
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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Api(tags = "SkillUsers", description = "API allowing queries of relationships from skills to users")
@RestController
public class SkillUserQueryController {
	private UserSkillQueryService userSkillQueryService;

	public SkillUserQueryController(UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
	}

	@ApiOperation(value = "Get all users related to a specific skill",
			notes = "Get all users currently related to a specific skill, each including the user's current skill " +
					"level, desired skill level and priority to reach the desired level. The list is unsorted. " +
					"Optionally allows to specify a minimum priority to retrieve a filtered result.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/skills/{skillId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SkillUserResponse> getSkillUsers(@PathVariable("skillId") String skillId,
												 @RequestParam(value = "minPriority", required = false)
														 Integer minPriority) {
		return userSkillQueryService.getUserSkillsBySkillId(skillId, minPriority)
				.map(userSkill -> new SkillUserResponse()
						.user(new UserResponse()
								.id(userSkill.getUser().getId())
								.userName(userSkill.getUser().getUserName())
								.firstName(userSkill.getUser().getFirstName())
								.lastName(userSkill.getUser().getLastName())
								.email(userSkill.getUser().getEmail()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority()))
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific user related to a specific skill",
			notes = "Get a specific user currently related to a specific skill including the user's current skill " +
					"level, desired skill level and priority to reach the desired level.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. skill does not exist or user not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/skills/{skillId}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SkillUserResponse getSkillUsers(@PathVariable("skillId") String skillId,
										   @PathVariable("userId") String userId) {
		return userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skillId)
				.map(userSkill -> new SkillUserResponse()
						.user(new UserResponse()
								.id(userSkill.getUser().getId())
								.userName(userSkill.getUser().getUserName())
								.firstName(userSkill.getUser().getFirstName())
								.lastName(userSkill.getUser().getLastName())
								.email(userSkill.getUser().getEmail()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority()))
				.orElseThrow(() -> new IllegalArgumentException(
						format("Skill with ID '%s' not related to user with ID '%s'", skillId, userId)));
	}
}
