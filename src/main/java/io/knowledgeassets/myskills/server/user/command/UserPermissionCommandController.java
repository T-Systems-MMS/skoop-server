package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.aspect.CheckBindingResult;
import io.knowledgeassets.myskills.server.user.UserPermissionRequest;
import io.knowledgeassets.myskills.server.user.UserPermissionResponse;
import io.knowledgeassets.myskills.server.user.command.ReplaceUserPermissionListCommand.UserPermissionEntry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Users", description = "API allowing modifications of users")
@RestController
public class UserPermissionCommandController {
	private UserPermissionCommandService userPermissionCommandService;

	public UserPermissionCommandController(UserPermissionCommandService userPermissionCommandService) {
		this.userPermissionCommandService = userPermissionCommandService;
	}

	@ApiOperation(
			value = "Replace the list of granted user permissions",
			notes = "Replace the entire list of granted user permissions for the user with the one in the request."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/permissions",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public List<UserPermissionResponse> replaceUserPermissions(@PathVariable("userId") String userId,
															   @Valid @RequestBody List<UserPermissionRequest> userPermissionRequests,
															   BindingResult bindingResult) {
		return userPermissionCommandService.replaceUserPermissions(
				ReplaceUserPermissionListCommand.builder()
						.ownerId(userId)
						.userPermissions(userPermissionRequests.stream()
								.map(userPermissionRequest -> UserPermissionEntry.builder()
										.scope(userPermissionRequest.getScope())
										.authorizedUserIds(userPermissionRequest.getAuthorizedUserIds())
										.build())
								.collect(toList()))
						.build())
				.map(UserPermissionResponse::of)
				.collect(toList());
	}
}
