package com.tsmms.skoop.security;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.user.query.UserQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static com.tsmms.skoop.exception.enums.Model.USER;
import static com.tsmms.skoop.security.JwtClaims.SKOOP_USER_ID;

@Api(tags = "MyIdentity")
@RestController
public class MyIdentityController {
	private final UserQueryService userQueryService;

	public MyIdentityController(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	@ApiOperation(
			value = "Get identity of the authenticated user",
			notes = "Get the identity details of the authenticated user including user ID, user name, granted roles " +
					"and additional user data."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Authentication failed, e.g. missing/invalid access token"),
			@ApiResponse(code = 404, message = "User identity for given access token not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/my-identity", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserIdentityResponse getMyIdentity(@ApiIgnore @AuthenticationPrincipal Jwt jwt) {
		String userId = jwt.getClaimAsString(SKOOP_USER_ID);
		return userQueryService.getUserById(userId)
				.map(user -> UserIdentityResponse.builder()
						.userId(user.getId())
						.userName(user.getUserName())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.email(user.getEmail())
						.notificationCount(199) // TODO: get value from service
						.build())
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(USER)
						.searchParamsMap(new String[]{"id", userId})
						.build());
	}
}
