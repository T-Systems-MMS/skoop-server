package com.tsmms.skoop.skillgroup.command;

import com.tsmms.skoop.aspect.CheckBindingResult;
import com.tsmms.skoop.exception.BusinessException;
import com.tsmms.skoop.skillgroup.SkillGroup;
import com.tsmms.skoop.skillgroup.SkillGroupRequest;
import com.tsmms.skoop.skillgroup.SkillGroupResponse;
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

@Api(tags = "SkillGroups")
@RestController
public class SkillGroupCommandController {
	private SkillGroupCommandService skillGroupCommandService;

	public SkillGroupCommandController(SkillGroupCommandService skillGroupCommandService) {
		this.skillGroupCommandService = skillGroupCommandService;
	}

	@ApiOperation(value = "Create a new skill group",
			notes = "Create a new skill group in the system. The skill group name must not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or skill name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	// TODO: Change API path to qualified name "/skill-groups"
	@PostMapping(path = "/groups",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public ResponseEntity<SkillGroupResponse> createGroup(@Valid @RequestBody SkillGroupRequest request,
														  BindingResult bindingResult) {
		SkillGroup skillGroup = null;
		try {
			skillGroup = skillGroupCommandService.createGroup(request.getName(), request.getDescription());
		} catch (BusinessException e) {
			e.setDebugMessage("An exception has occurred in creating a skill group!");
			e.setSuggestion("Make sure that skill group with the given name doesn't already exists!");
			throw e;
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(SkillGroupResponse.builder()
				.id(skillGroup.getId())
				.name(skillGroup.getName())
				.description(skillGroup.getDescription())
				.build());
	}

	@ApiOperation(value = "Update an existing skill group",
			notes = "Update an existing skill group in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	// TODO: Change API path to qualified name "/skill-groups"
	@PutMapping(path = "/groups/{skillGroupId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public SkillGroupResponse updateSkill(@PathVariable("skillGroupId") String skillGroupId, @Valid @RequestBody SkillGroupRequest request,
										  BindingResult bindingResult) {
		SkillGroup skillGroup = skillGroupCommandService.updateGroup(skillGroupId, request.getName(), request.getDescription());
		return SkillGroupResponse.builder()
				.id(skillGroup.getId())
				.name(skillGroup.getName())
				.description(skillGroup.getDescription())
				.build();
	}

	@ApiOperation(value = "Delete an existing skill group",
			notes = "Delete an existing skill group from the system. All relationships with skills will be discarded!")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('ADMIN')")
	// TODO: Change API path to qualified name "/skill-groups"
	@DeleteMapping(path = "/groups/{skillGroupId}")
	public ResponseEntity<Void> deleteSkill(@PathVariable("skillGroupId") String skillGroupId) {
		skillGroupCommandService.deleteGroup(skillGroupId);
		return ResponseEntity.noContent().build();
	}
}
