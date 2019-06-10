package com.tsmms.skoop.user.query;

import com.google.common.base.Enums;
import com.tsmms.skoop.user.GlobalUserPermission;
import com.tsmms.skoop.user.GlobalUserPermissionResponse;
import com.tsmms.skoop.user.GlobalUserPermissionScope;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
	@GetMapping(path = "/users/{userId}/outbound-global-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GlobalUserPermissionResponse> getOutboundGlobalUserPermissions(@PathVariable("userId") String userId,
																	   @RequestParam(name = "scope", required = false) String scope) {
		final Optional<GlobalUserPermissionScope> globalUserPermissionScope = scope != null ? Enums.getIfPresent(GlobalUserPermissionScope.class, scope)
				.transform(Optional::of).or(Optional.empty()) : Optional.empty();
		final Stream<GlobalUserPermission> globalUserPermissionStream;
		if (globalUserPermissionScope.isPresent()) {
			globalUserPermissionStream = globalUserPermissionQueryService.getOutboundGlobalUserPermissionsByScope(userId, globalUserPermissionScope.get());
		} else {
			globalUserPermissionStream = globalUserPermissionQueryService.getOutboundGlobalUserPermissions(userId);
		}
		return globalUserPermissionStream.map(GlobalUserPermissionResponse::of).collect(toList());
	}

	@ApiOperation(
			value = "Get all user global permissions granted to the user.",
			notes = "Get all user global permissions the given user has been granted by other users in the system."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/inbound-global-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GlobalUserPermissionResponse> getInboundGlobalUserPermissions(@PathVariable("userId") String userId,
																	   @RequestParam(name = "scope", required = false) String scope) {
		final Optional<GlobalUserPermissionScope> globalUserPermissionScope = scope != null ? Enums.getIfPresent(GlobalUserPermissionScope.class, scope)
				.transform(Optional::of).or(Optional.empty()) : Optional.empty();
		final Stream<GlobalUserPermission> globalUserPermissionStream;
		if (globalUserPermissionScope.isPresent()) {
			globalUserPermissionStream = globalUserPermissionQueryService.getInboundGlobalUserPermissionsByScope(userId, globalUserPermissionScope.get());
		} else {
			globalUserPermissionStream = globalUserPermissionQueryService.getInboundGlobalUserPermissions(userId);
		}
		return globalUserPermissionStream.map(GlobalUserPermissionResponse::of).collect(toList());
	}

}
