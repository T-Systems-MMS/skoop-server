package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.user.UserRequest;
import io.knowledgeassets.myskills.server.user.UserResponse;
import io.knowledgeassets.myskills.server.user.query.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Users", description = "API allowing modifications of users")
@RestController
public class UserCommandController {
	private UserCommandService userCommandService;

	public UserCommandController(UserCommandService userCommandService) {
		this.userCommandService = userCommandService;
	}

	@ApiOperation(value = "Create a new user",
			notes = "Create a new user in the system. The user name must not exist yet.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or user name exists"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = "/users",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
		User user = userCommandService.createUser(request.getUserName(), request.getFirstName(), request.getLastName(),
				request.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail()));
	}

	@ApiOperation(value = "Update an existing user",
			notes = "Update an existing user in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@PutMapping(path = "/users/{userId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody UserRequest request) {
		User user = userCommandService.updateUser(userId, request.getUserName(), request.getFirstName(),
				request.getLastName(), request.getEmail());
		return new UserResponse()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail());
	}

	@ApiOperation(value = "Delete an existing user",
			notes = "Delete an existing user from the system. All relationships with skills will be discarded!")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {
		userCommandService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
}
