package com.tsmms.skoop.user.command;

import com.tsmms.skoop.user.command.ReplaceGlobalUserPermissionListCommand.GlobalUserPermissionEntry;
import com.tsmms.skoop.user.GlobalUserPermissionRequest;
import com.tsmms.skoop.user.GlobalUserPermissionResponse;
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

@Api(tags = "GlobalUserPermissions")
@RestController
public class GlobalUserPermissionCommandController {

	private final GlobalUserPermissionCommandService globalUserPermissionCommandService;

	public GlobalUserPermissionCommandController(GlobalUserPermissionCommandService globalUserPermissionCommandService) {
		this.globalUserPermissionCommandService = requireNonNull(globalUserPermissionCommandService);
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
	public List<GlobalUserPermissionResponse> replaceGlobalPermissions(@PathVariable("userId") String userId,
																	   @Valid @RequestBody List<GlobalUserPermissionRequest> globalUserPermissionRequests) {

		return globalUserPermissionCommandService.replaceGlobalUserPermissions(
				ReplaceGlobalUserPermissionListCommand.builder()
						.ownerId(userId)
						.globalPermissions(globalUserPermissionRequests.stream()
								.map(globalUserPermissionRequest -> GlobalUserPermissionEntry.builder()
										.scope(globalUserPermissionRequest.getScope())
										.build()
								)
								.collect(toSet())
						)
						.build()
		).map(GlobalUserPermissionResponse::of).collect(toList());
	}

}
