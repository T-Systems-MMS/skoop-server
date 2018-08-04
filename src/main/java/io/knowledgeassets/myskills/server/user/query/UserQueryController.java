package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.user.UserResponse;
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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Api(tags = "Users", description = "API allowing queries of users")
@RestController
public class UserQueryController {
	private UserQueryService userQueryService;

	public UserQueryController(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	@ApiOperation(value = "Get all users",
			notes = "Get all users currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserResponse> getUsers() {
		return userQueryService.getUsers()
				.map(user -> new UserResponse()
						.id(user.getId())
						.userName(user.getUserName())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.email(user.getEmail()))
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific user",
			notes = "Get a specific user currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResponse getUserById(@PathVariable("userId") String userId) {
		return userQueryService.getUserById(userId)
				.map(user -> new UserResponse()
						.id(user.getId())
						.userName(user.getUserName())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.email(user.getEmail()))
				.orElseThrow(() -> new IllegalArgumentException(format("User with ID '%s' not found", userId)));
	}
}
