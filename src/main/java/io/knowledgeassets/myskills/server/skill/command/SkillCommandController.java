package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRequest;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<SkillResponse> createSkill(@RequestBody SkillRequest request) {
		Skill skill = skillCommandService.createSkill(request.getName(), request.getDescription());
		return ResponseEntity.status(HttpStatus.CREATED).body(new SkillResponse()
				.id(skill.getId())
				.name(skill.getName())
				.description(skill.getDescription()));
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
	public SkillResponse updateSkill(@PathVariable("skillId") String skillId, @RequestBody SkillRequest request) {
		Skill skill = skillCommandService.updateSkill(skillId, request.getName(), request.getDescription());
		return new SkillResponse().id(skill.getId()).name(skill.getName()).description(skill.getDescription());
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
}
