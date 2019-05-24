package com.tsmms.skoop.userproject.query;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.UserNotAuthorizedException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.userproject.UserProjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.lang.String.format;
import static com.tsmms.skoop.user.UserPermissionScope.READ_USER_SKILLS;

@Api(tags = "UserProjects")
@RestController
public class UserProjectQueryController {

	private final UserProjectQueryService userProjectQueryService;
	private final CurrentUserService currentUserService;
	private final UserQueryService userQueryService;
	private final UserPermissionQueryService userPermissionQueryService;

	public UserProjectQueryController(UserProjectQueryService userProjectQueryService,
									  CurrentUserService currentUserService,
									  UserQueryService userQueryService,
									  UserPermissionQueryService userPermissionQueryService) {
		this.userProjectQueryService = requireNonNull(userProjectQueryService);
		this.currentUserService = requireNonNull(currentUserService);
		this.userQueryService = requireNonNull(userQueryService);
		this.userPermissionQueryService = requireNonNull(userPermissionQueryService);
	}

	@ApiOperation(value = "Get all projects related to a specific user",
			notes = "Get all projects user takes part in or took part in before.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/users/{userId}/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserProjectResponse>> getUserProjects(@PathVariable("userId") String userId) {
		final User user = userQueryService.getUserById(userId)
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", userId};
					return NoSuchResourceException.builder()
							.model(Model.USER)
							.searchParamsMap(searchParamsMap)
							.build();
				});
		if (currentUserService.isAuthenticatedUserId(userId) ||
				userPermissionQueryService.hasUserPermission(userId, currentUserService.getCurrentUserId(), READ_USER_SKILLS) ||
				(user.getManager() != null && currentUserService.getCurrentUserId().equals(user.getManager().getId()))) {
			final List<UserProjectResponse> userProjects = userProjectQueryService.getUserProjects(userId)
					.map(UserProjectResponse::of)
					.collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.OK).body(userProjects);
		} else {
			throw new UserNotAuthorizedException(format("The user is not authorized to view projects of the user with ID %s", userId));
		}
	}

	@ApiOperation(value = "Get specific project related to a specific user",
			notes = "Get specific project user takes part in or took part in before.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found, e.g. user does not exist or skill not assigned"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId) or hasUserPermission(#userId, 'READ_USER_SKILLS')")
	@GetMapping(path = "/users/{userId}/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProjectResponse> getUserProject(@PathVariable("userId") String userId,
																	 @PathVariable("projectId") String projectId) {
		final UserProjectResponse userProject = userProjectQueryService.getUserProjectByUserIdAndProjectId(userId, projectId)
				.map(UserProjectResponse::of)
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(Model.USER_PROJECT)
						.searchParamsMap(new String[]{"userId", userId, "projectId", projectId})
						.build());
		return ResponseEntity.status(HttpStatus.OK).body(userProject);
	}

}
