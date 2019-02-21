package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.aspect.CheckBindingResult;
import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Api(tags = "UserSkills")
@RestController
public class UserSkillCommandController {
	private UserSkillCommandService userSkillCommandService;

	public UserSkillCommandController(UserSkillCommandService userSkillCommandService) {
		this.userSkillCommandService = userSkillCommandService;
	}

	@ApiOperation(value = "Create a new relationship between a user and a skill",
			notes = "Create a new relationship between a specific user and a specific skill. Either a skillId or " +
					"a skillName must be passed as input. If the skillId is given a specific skill with this ID " +
					"must exist in the system. If the skillName is given a skill with this name will be created if " +
					"it does not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@PostMapping(path = "/users/{userId}/skills",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public ResponseEntity<UserSkillResponse> createUserSkill(@PathVariable("userId") String userId,
															 @Valid @RequestBody CreateUserSkillRequest request,
															 BindingResult bindingResult) {
		UserSkill userSkill;
		if (isNotBlank(request.getSkillId())) {
			userSkill = userSkillCommandService.createUserSkillBySkillId(userId, request.getSkillId(),
					request.getCurrentLevel(), request.getDesiredLevel(), request.getPriority());
		} else if (isNotBlank(request.getSkillName())) {
			userSkill = userSkillCommandService.createUserSkillBySkillName(userId, request.getSkillName(),
					request.getCurrentLevel(), request.getDesiredLevel(), request.getPriority());
		} else {
			throw EmptyInputException.builder()
					.message("Either the property 'skillId' or 'skillName' is required!")
					.build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(UserSkillResponse.builder()
				.skill(SkillResponse.of(userSkill.getSkill()))
				.currentLevel(userSkill.getCurrentLevel())
				.desiredLevel(userSkill.getDesiredLevel())
				.priority(userSkill.getPriority())
				.build());
	}

	@ApiOperation(value = "Update a relationship between a user and a skill",
			notes = "Update an existing relationship between a specific user and a specific skill.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/skills/{skillId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public UserSkillResponse updateUserSkill(@PathVariable("userId") String userId,
											 @PathVariable("skillId") String skillId,
											 @Valid @RequestBody UpdateUserSkillRequest request,
											 BindingResult bindingResult) {
		UserSkill userSkill = null;
		try {
			userSkill = userSkillCommandService.getUserSkill(userId, skillId);
			userSkill = userSkillCommandService.updateUserSkill(request.getCurrentLevel(),
					request.getDesiredLevel(), request.getPriority(), userSkill);
		} catch (BusinessException e) {
			e.setDebugMessage("An exception has occurred in updating a relationship between a user and a skill!");
			e.setSuggestion("Make sure that the skill is related to the user!");
			throw e;
		}
		return UserSkillResponse.builder()
				.skill(SkillResponse.of(userSkill.getSkill()))
				.currentLevel(userSkill.getCurrentLevel())
				.desiredLevel(userSkill.getDesiredLevel())
				.priority(userSkill.getPriority())
				.build();
	}

	@ApiOperation(value = "Delete a relationship between a user and a skill",
			notes = "Delete an existing relationship between a specific user and a specific skill.")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@DeleteMapping(path = "/users/{userId}/skills/{skillId}")
	public ResponseEntity<Void> deleteUserSkill(@PathVariable("userId") String userId,
												@PathVariable("skillId") String skillId) {
		userSkillCommandService.deleteUserSkill(userSkillCommandService.getUserSkill(userId, skillId));
		return ResponseEntity.noContent().build();
	}
}
