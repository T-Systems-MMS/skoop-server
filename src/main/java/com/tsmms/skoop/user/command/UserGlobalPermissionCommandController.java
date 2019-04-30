package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.command.ReplaceUserGlobalPermissionListCommand.UserGlobalPermissionEntry;
import com.tsmms.skoop.user.GlobalPermissionRequest;
import com.tsmms.skoop.user.GlobalPermissionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

@Api(tags = "UserGlobalPermissions")
@RestController
public class UserGlobalPermissionCommandController {

	private final UserGlobalPermissionCommandService userGlobalPermissionCommandService;

	public UserGlobalPermissionCommandController(UserGlobalPermissionCommandService userGlobalPermissionCommandService) {
		this.userGlobalPermissionCommandService = requireNonNull(userGlobalPermissionCommandService);
	}

	@ApiOperation(
			value = "Replace the list of user global permissions granted by user.",
			notes = "Replace the entire list of user global permissions granted by user with a specified id."
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
	@PutMapping(path = "/users/{userId}/global-permissions",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GlobalPermissionResponse> replaceGlobalPermissions(@PathVariable("userId") String userId,
																   @Valid @RequestBody List<GlobalPermissionRequest> globalPermissionRequests) {

		return userGlobalPermissionCommandService.replaceUserGlobalPermissions(
				ReplaceUserGlobalPermissionListCommand.builder()
				.ownerId(userId)
				.globalPermissions(globalPermissionRequests.stream()
						.map(globalPermissionRequest -> UserGlobalPermissionEntry.builder()
								.scope(globalPermissionRequest.getScope())
								.build()
						)
						.collect(toSet())
				)
				.build()
		).map(GlobalPermissionResponse::of).collect(toList());
	}

}
