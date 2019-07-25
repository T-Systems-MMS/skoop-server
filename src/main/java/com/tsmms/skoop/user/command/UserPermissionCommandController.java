package com.tsmms.skoop.user.command;

import com.tsmms.skoop.aspect.CheckBindingResult;
import com.tsmms.skoop.user.UserPermissionRequest;
import com.tsmms.skoop.user.UserPermissionResponse;
import com.tsmms.skoop.user.command.ReplaceUserPermissionListCommand.UserPermissionEntry;
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

@Api(tags = "UserPermissions")
@RestController
public class UserPermissionCommandController {

	private UserCommandService userCommandService;

	public UserPermissionCommandController(UserCommandService userCommandService) {
		this.userCommandService = userCommandService;
	}

	@ApiOperation(
			value = "Replace the list of user permissions granted by user.",
			notes = "Replace the entire list of user permissions granted by user with a specified id."
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
	@PutMapping(path = "/users/{userId}/outbound-permissions",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@CheckBindingResult
	public List<UserPermissionResponse> replaceOutboundUserPermissions(@PathVariable("userId") String userId,
																	   @Valid @RequestBody List<UserPermissionRequest> userPermissionRequests,
																	   BindingResult bindingResult) {
		return userCommandService.replaceOutboundUserPermissions(
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
