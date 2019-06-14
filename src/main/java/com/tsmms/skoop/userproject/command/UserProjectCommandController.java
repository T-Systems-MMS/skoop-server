package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.exception.EmptyInputException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.UserNotAuthorizedException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectResponse;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.Objects.requireNonNull;
import static java.lang.String.format;

@Api(tags = "UserProjects")
@RestController
public class UserProjectCommandController {

	private final UserProjectCommandService userProjectCommandService;
	private final SkillQueryService skillQueryService;
	private final CurrentUserService currentUserService;
	private final UserQueryService userQueryService;

	public UserProjectCommandController(UserProjectCommandService userProjectCommandService,
										SkillQueryService skillQueryService,
										CurrentUserService currentUserService,
										UserQueryService userQueryService) {
		this.userProjectCommandService = requireNonNull(userProjectCommandService);
		this.skillQueryService = requireNonNull(skillQueryService);
		this.currentUserService = requireNonNull(currentUserService);
		this.userQueryService = requireNonNull(userQueryService);
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
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
		final UserProject result = userProjectCommandService.assignProjectToUser(request.getProjectName(), userId, userProject);
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
	@PreAuthorize("isAuthenticated()")
	@PutMapping(path = "/users/{userId}/projects/{projectId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProjectResponse> updateUserProject(@PathVariable("userId") String userId,
																 @PathVariable("projectId") String projectId,
																 @Valid @RequestBody UpdateUserProjectRequest request) {
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final UpdateUserProjectCommand updateUserProjectCommand = updateUserProjectRequestToUpdateUserProjectCommand(request);
		final boolean isUserManager = user.getManager() != null && currentUserService.getCurrentUserId().equals(user.getManager().getId());
		if (updateUserProjectCommand.isApproved() && !isUserManager) {
			throw new UserNotAuthorizedException(format("Only the manager of the user with ID %s can approve user's project memberships.", userId));
		}
		if (currentUserService.isAuthenticatedUserId(userId) || isUserManager) {
			final UserProject result = userProjectCommandService.updateUserProject(userId, projectId, updateUserProjectCommand);
			return ResponseEntity.status(HttpStatus.OK)
					.body(UserProjectResponse.of(result));
		} else {
			throw new UserNotAuthorizedException(format("The authenticated user is not authorized to update the project membership of the user with ID %s", userId));
		}
	}

	@ApiOperation(value = "Update a relationships between a user and her / his projects",
			notes = "Update an existing relationships between a specific user and her / his projects.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@PutMapping(path = "/users/{userId}/projects",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<UserProjectResponse>> updateUserProjects(@PathVariable("userId") String userId,
																			  @Valid @RequestBody Collection<UpdateUserProjectRequest> request) {
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Set<UpdateUserProjectCommand> updateUserProjectCommands = request.stream().map(this::updateUserProjectRequestToUpdateUserProjectCommand)
				.collect(toSet());
		final boolean isUserManager = user.getManager() != null && currentUserService.getCurrentUserId().equals(user.getManager().getId());
		final Set<UserProject> userProjects = new HashSet<>(updateUserProjectCommands.size());
		updateUserProjectCommands.forEach(updateUserProjectCommand -> {
			if (updateUserProjectCommand.isApproved() && !isUserManager) {
				throw new UserNotAuthorizedException(format("Only the manager of the user with ID %s can approve user's project memberships.", userId));
			}
			if (currentUserService.isAuthenticatedUserId(userId) || isUserManager) {
				if (updateUserProjectCommand.getProjectId() == null) {
					throw new EmptyInputException("The project ID cannot be null.");
				}
				final UserProject result = userProjectCommandService.updateUserProject(userId, updateUserProjectCommand.getProjectId(), updateUserProjectCommand);
				userProjects.add(result);
			} else {
				throw new UserNotAuthorizedException(format("The authenticated user is not authorized to update the project membership of the user with ID %s", userId));
			}
		});
		return ResponseEntity.status(HttpStatus.OK)
				.body(userProjects.stream().map(UserProjectResponse::of).collect(toSet()));
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

	private UpdateUserProjectCommand updateUserProjectRequestToUpdateUserProjectCommand(UpdateUserProjectRequest request) {
		return UpdateUserProjectCommand.builder()
				.projectId(request.getProjectId())
				.role(request.getRole())
				.tasks(request.getTasks())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.approved(Boolean.TRUE.equals(request.getApproved()))
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
	}

}
