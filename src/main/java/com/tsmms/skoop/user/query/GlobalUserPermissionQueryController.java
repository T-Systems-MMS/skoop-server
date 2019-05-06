package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.GlobalUserPermissionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Api(tags = "GlobalUserPermissions")
@RestController
public class GlobalUserPermissionQueryController {

	private final GlobalUserPermissionQueryService globalUserPermissionQueryService;

	public GlobalUserPermissionQueryController(GlobalUserPermissionQueryService globalUserPermissionQueryService) {
		this.globalUserPermissionQueryService = requireNonNull(globalUserPermissionQueryService);
	}

	@ApiOperation(
			value = "Get all user global permissions granted by the user.",
			notes = "Get all user global permissions the given user has granted to other users in the system."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/global-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GlobalUserPermissionResponse> getGlobalUserPermissions(@PathVariable("userId") String userId) {
		return globalUserPermissionQueryService.getGlobalUserPermissions(userId)
				.map(GlobalUserPermissionResponse::of)
				.collect(toList());
	}

}
