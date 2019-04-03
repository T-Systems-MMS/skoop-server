package com.tsmms.skoop.userskill.query;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.SkillResponse;
import com.tsmms.skoop.user.UserSimpleResponse;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.userskill.UserSkillResponse;
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

@Api(tags = "UserSkills")
@RestController
public class UserSkillQueryController {
	private UserSkillQueryService userSkillQueryService;

	public UserSkillQueryController(UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
	}

	@ApiOperation(value = "Get all skills related to a specific user",
			notes = "Get all skills currently related to a specific user, each including the user's current skill " +
					"level, desired skill level and priority to reach the desired level. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId) or hasUserPermission(#userId, 'READ_USER_SKILLS')")
	@GetMapping(path = "/users/{userId}/skills", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillResponse> getUserSkills(@PathVariable("userId") String userId) {
		return userSkillQueryService.getUserSkillsByUserId(userId)
				.map(userSkill -> UserSkillResponse.builder()
						.skill(SkillResponse.of(userSkill.getSkill()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.build())
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific skill related to a specific user",
			notes = "Get a specific skill currently related to a specific user including the user's current skill " +
					"level, desired skill level and priority to reach the desired level.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId) or hasUserPermission(#userId, 'READ_USER_SKILLS')")
	@GetMapping(path = "/users/{userId}/skills/{skillId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserSkillResponse getUserSkill(@PathVariable("userId") String userId,
										  @PathVariable("skillId") String skillId) {
		return userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skillId)
				.map(userSkill -> UserSkillResponse.builder()
						.skill(SkillResponse.of(userSkill.getSkill()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.build())
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(Model.USER_SKILL)
						.searchParamsMap(new String[]{"userId", userId, "skillId", skillId})
						.build());
	}

	@ApiOperation(value = "Get all coaches for a specific skill related to a specific user",
			notes = "Get all potential coaches for a specific skill currently related to a specific user. Coaches " +
					"are those users who have the given skill at a current level higher/equal the desired level of " +
					"the given user.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/skills/{skillId}/coaches", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSimpleResponse> getUserSkillCoaches(@PathVariable("userId") String userId,
														@PathVariable("skillId") String skillId) {
		return userSkillQueryService.getCoachesByUserIdAndSkillId(userId, skillId)
				.map(UserSimpleResponse::of)
				.collect(toList());
	}
}
