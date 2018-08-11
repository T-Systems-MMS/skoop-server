package io.knowledgeassets.myskills.server.security;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static java.util.stream.Collectors.toList;

@Api(tags = "MyIdentity", description = "API allowing queries of the user identity for the authenticated user")
@RestController
public class MyIdentityController {
	@ApiOperation(value = "Get identity of the authenticated user",
			notes = "Get the identity details of the authenticated user including user ID, user name, granted roles " +
					"and additional user data.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Authentication failed, e.g. missing/wrong credentials"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/my-identity", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserIdentityResponse getMyIdentity(@ApiIgnore @AuthenticationPrincipal UserIdentity userIdentity) {
		return new UserIdentityResponse()
				.userId(userIdentity.getUserId())
				.userName(userIdentity.getUserName())
				.firstName(userIdentity.getFirstName())
				.lastName(userIdentity.getLastName())
				.email(userIdentity.getEmail())
				.roles(userIdentity.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()));
	}
}
