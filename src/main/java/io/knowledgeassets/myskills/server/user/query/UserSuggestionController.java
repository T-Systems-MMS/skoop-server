package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.user.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "UserSuggestions")
@RestController
public class UserSuggestionController {
	private UserQueryService userQueryService;

	public UserSuggestionController(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	@ApiOperation(
			value = "Get user suggestions",
			notes = "Get user suggestions for the given search term. The term is looked up in the user name, first " +
					"name and last name. The list is sorted by user name in alphabetical order."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/user-suggestions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserResponse> getUserSuggestions(@RequestParam("search") String search) {
		return userQueryService.getUsersBySearchTerm(search)
				.map(UserResponse::of)
				.collect(toList());
	}
}
