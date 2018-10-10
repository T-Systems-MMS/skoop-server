package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.aspect.CheckBindingResult;
import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRequest;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Skills", description = "API allowing modifications of skills")
@RestController
public class SkillCommandController {
	private SkillCommandService skillCommandService;

	public SkillCommandController(SkillCommandService skillCommandService) {
		this.skillCommandService = skillCommandService;
	}

	@ApiOperation(value = "Create a new skill",
			notes = "Create a new skill in the system. The skill name must not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or skill name exists"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = "/skills",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public ResponseEntity<SkillResponse> createSkill(@Valid @RequestBody SkillRequest request,
													 BindingResult bindingResult) {
		Skill skill = null;
		try {
			skill = skillCommandService.createSkill(request.getName(), request.getDescription(), request.getSkillGroups());
		} catch (BusinessException e) {
			e.setDebugMessage("An exception has occurred in creating a skill!");
			e.setSuggestion("Make sure that skill with the given name doesn't already exists!");
			throw e;
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(SkillResponse.builder()
				.id(skill.getId())
				.name(skill.getName())
				.description(skill.getDescription())
				.skillGroups(getGroups(skill.getSkillGroups()))
				.build());
	}

	@ApiOperation(value = "Update an existing skill",
			notes = "Update an existing skill in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@PutMapping(path = "/skills/{skillId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public SkillResponse updateSkill(@PathVariable("skillId") String skillId, @Valid @RequestBody SkillRequest request,
									 BindingResult bindingResult) {
		Skill skill = skillCommandService.updateSkill(skillId, request.getName(), request.getDescription(), request.getSkillGroups());
		return SkillResponse.builder()
				.id(skill.getId())
				.name(skill.getName())
				.description(skill.getDescription())
				.skillGroups(getGroups(skill.getSkillGroups()))
				.build();
	}

	@ApiOperation(value = "Delete an existing skill",
			notes = "Delete an existing skill from the system. All relationships with users will be discarded!")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/skills/{skillId}")
	public ResponseEntity<Void> deleteSkill(@PathVariable("skillId") String skillId) {
		skillCommandService.deleteSkill(skillId);
		return ResponseEntity.noContent().build();
	}

	private List<String> getGroups(List<SkillGroup> skillGroups) {
		if (!CollectionUtils.isEmpty(skillGroups)) {
			return skillGroups.stream().map(SkillGroup::getName).collect(toList());
		} else {
			return null;
		}
	}
}
