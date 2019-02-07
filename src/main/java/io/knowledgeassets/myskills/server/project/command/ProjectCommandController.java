package io.knowledgeassets.myskills.server.project.command;

import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.project.ProjectRequest;
import io.knowledgeassets.myskills.server.project.ProjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Projects", description = "API allowing modifications of projects")
@RestController
public class ProjectCommandController {

	private final ProjectCommandService projectCommandService;

	public ProjectCommandController(ProjectCommandService projectCommandService) {
		this.projectCommandService = projectCommandService;
	}

	@ApiOperation(value = "Create new project",
			notes = "Create new project in the system. The project name must not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or project name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@PostMapping(path = "/projects",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request) {
		final Project project = projectCommandService.create(convertProjectRequestToProjectDomain(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.of(project));
	}

	@ApiOperation(value = "Update an existing project",
			notes = "Update an existing project in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@PutMapping(path = "/projects/{projectId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProjectResponse> updateProject(@PathVariable("projectId") String projectId, @Valid @RequestBody ProjectRequest request) {
		final Project project = convertProjectRequestToProjectDomain(request);
		project.setId(projectId);
		final Project result = projectCommandService.update(project);
		return ResponseEntity.status(HttpStatus.OK).body(ProjectResponse.of(result));
	}

	@ApiOperation(value = "Delete an existing project",
			notes = "Delete an existing project from the system. All relationships with users will be discarded!")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/projects/{projectId}")
	public ResponseEntity<Void> deleteProject(@PathVariable("projectId") String projectId) {
		projectCommandService.delete(projectId);
		return ResponseEntity.noContent().build();
	}

	private Project convertProjectRequestToProjectDomain(ProjectRequest projectRequest) {
		return Project.builder()
				.name(projectRequest.getName())
				.customer(projectRequest.getCustomer())
				.industrySector(projectRequest.getIndustrySector())
				.description(projectRequest.getDescription())
				.build();
	}

}
