package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserResponse;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;
import static io.knowledgeassets.myskills.server.user.UserPermissionScope.READ_USER_SKILLS;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Api(tags = "SkillUsers", description = "API allowing queries of relationships from skills to users")
@RestController
public class SkillUserQueryController {
	private UserSkillQueryService userSkillQueryService;
	private UserPermissionQueryService userPermissionQueryService;

	public SkillUserQueryController(UserSkillQueryService userSkillQueryService,
									UserPermissionQueryService userPermissionQueryService) {
		this.userSkillQueryService = userSkillQueryService;
		this.userPermissionQueryService = userPermissionQueryService;
	}

	@ApiOperation(value = "Get all users related to a specific skill",
			notes = "Get all users currently related to a specific skill, each including the user's current skill " +
					"level, desired skill level and priority to reach the desired level. The list is unsorted. " +
					"Optionally allows to specify a minimum priority to retrieve a filtered result.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/skills/{skillId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SkillUserResponse> getSkillUsers(@PathVariable("skillId") String skillId,
												 @RequestParam(value = "minPriority", required = false)
														 Integer minPriority,
												 @ApiIgnore @AuthenticationPrincipal Jwt jwt)
			throws BusinessException {
		// Create a whitelist of those user IDs who allowed the principal read access to their skill relationships.
		Set<String> allowedUserIds = userPermissionQueryService.getUsersWhoGrantedPermission(
				jwt.getClaimAsString(MYSKILLS_USER_ID), READ_USER_SKILLS).map(User::getId).collect(toSet());
		allowedUserIds.add(jwt.getClaimAsString(MYSKILLS_USER_ID));

		return userSkillQueryService.getBySkillId(skillId, minPriority)
				.filter(userSkill -> allowedUserIds.contains(userSkill.getUser().getId()))
				.map(userSkill -> SkillUserResponse.builder()
						.user(UserSimpleResponse.of(userSkill.getUser()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.build())
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific user related to a specific skill",
			notes = "Get a specific user currently related to a specific skill including the user's current skill " +
					"level, desired skill level and priority to reach the desired level.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. skill does not exist or user not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId) or hasUserPermission(#userId, 'READ_USER_SKILLS')")
	@GetMapping(path = "/skills/{skillId}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SkillUserResponse getSkillUsers(@PathVariable("skillId") String skillId,
										   @PathVariable("userId") String userId) {
		return userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skillId)
				.map(userSkill -> SkillUserResponse.builder()
						.user(UserSimpleResponse.of(userSkill.getUser()))
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.build())
				.orElseThrow(() -> InvalidInputException.builder().message(
						format("Skill with ID '%s' is not related to user with ID '%s'", skillId, userId)).build());
	}
}
