package io.knowledgeassets.myskills.server.userproject.command;

import io.knowledgeassets.myskills.server.userproject.UserProject;
import io.knowledgeassets.myskills.server.userproject.UserProjectResponse;
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

@Api(tags = "UserProjects")
@RestController
public class UserProjectCommandController {

	private final UserProjectCommandService userProjectCommandService;

	public UserProjectCommandController(UserProjectCommandService userProjectCommandService) {
		this.userProjectCommandService = userProjectCommandService;
	}

	@ApiOperation(value = "Assign a project to a user.",
			notes = "Creates a new relationship between a specific user and a specific project.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@PostMapping(path = "/users/{userId}/projects",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProjectResponse> assignProjectToUser(@PathVariable("userId") String userId, @Valid @RequestBody AssignUserProjectRequest request) {
		final UserProject userProject = UserProject.builder()
				.tasks(request.getTasks())
				.role(request.getRole())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.build();
		final UserProject result = userProjectCommandService.assignProjectToUser(request.getProjectId(), userId, userProject);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(UserProjectResponse.of(result));
	}

	@ApiOperation(value = "Update a relationship between a user and a project",
			notes = "Update an existing relationship between a specific user and a specific project.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/projects/{projectId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProjectResponse> updateUserProject(@PathVariable("userId") String userId,
																 @PathVariable("projectId") String projectId,
																 @Valid @RequestBody UpdateUserProjectRequest request) {
		final UserProject result = userProjectCommandService.updateUserProject(userId, projectId, request.command());
		return ResponseEntity.status(HttpStatus.OK)
				.body(UserProjectResponse.of(result));
	}

	@ApiOperation(value = "Delete a relationship between a user and a project",
			notes = "Delete an existing relationship between a specific user and a specific project.")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@DeleteMapping(path = "/users/{userId}/projects/{projectId}")
	public ResponseEntity<Void> deleteUserProject(@PathVariable("userId") String userId,
												  @PathVariable("projectId") String projectId) {
		userProjectCommandService.deleteUserProject(userId, projectId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
