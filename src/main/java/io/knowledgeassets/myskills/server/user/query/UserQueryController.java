package io.knowledgeassets.myskills.server.user.query;

import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;
import static io.knowledgeassets.myskills.server.user.UserPermissionScope.READ_USER_SKILLS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Api(tags = "Users")
@RestController
public class UserQueryController {
	private UserQueryService userQueryService;
	private UserPermissionQueryService userPermissionQueryService;

	public UserQueryController(UserQueryService userQueryService,
							   UserPermissionQueryService userPermissionQueryService) {
		this.userQueryService = userQueryService;
		this.userPermissionQueryService = userPermissionQueryService;
	}

	@ApiOperation(
			value = "Get all users",
			notes = "Get all users currently stored in the system. The list is unsorted."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserResponse> getUsers(@ApiIgnore @AuthenticationPrincipal Jwt jwt) {
		final Set<String> allowedUserIds = getIdsOfUsersWhoGrantedPermissionToReadSkills(jwt);
		return userQueryService.getUsers()
				.map(user -> convertUserToUserResponse(user, allowedUserIds))
				.collect(toList());
	}

	@ApiOperation(
			value = "Get a specific user",
			notes = "Get a specific user currently stored in the system."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResponse getUserById(@PathVariable("userId") String userId,
									@ApiIgnore @AuthenticationPrincipal Jwt jwt) {
		final Set<String> allowedUserIds = getIdsOfUsersWhoGrantedPermissionToReadSkills(jwt);
		return userQueryService.getUserById(userId)
				.map(user -> convertUserToUserResponse(user, allowedUserIds))
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", userId};
					return NoSuchResourceException.builder()
							.model(Model.USER)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

	/**
	 * Converts user domain model to user response DTO.
	 * Extended user info fields are filled only for users who allowed to view their skills (or for the authenticated user).
	 * @param user - domain model to convert
	 * @param allowedUserIds - ids of user who allowed to view their skills (or for the authenticated user)
	 * @return user response DTO
	 */
	private UserResponse convertUserToUserResponse(User user, Set<String> allowedUserIds) {
		final UserResponse.UserResponseBuilder b = UserResponse.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.coach(user.getCoach());
		if (allowedUserIds.contains(user.getId())) {
			b.academicDegree(user.getAcademicDegree())
					.positionProfile(user.getPositionProfile())
					.summary(user.getSummary())
					.industrySectors(user.getIndustrySectors())
					.specializations(user.getSpecializations())
					.certificates(user.getCertificates())
					.languages(user.getLanguages());
		}
		return b.build();
	}

	/**
	 * Retrieves ids of users who granted the permission to read their skills to the authenticated user.
	 * @param jwt - JWT of authenticated user
	 * @return ids of allowed users
	 */
	private Set<String> getIdsOfUsersWhoGrantedPermissionToReadSkills(Jwt jwt) {
		final Set<String> allowedUserIds = userPermissionQueryService.getUsersWhoGrantedPermission(
				jwt.getClaimAsString(MYSKILLS_USER_ID), READ_USER_SKILLS).map(User::getId).collect(toSet());
		allowedUserIds.add(jwt.getClaimAsString(MYSKILLS_USER_ID));
		return allowedUserIds;
	}

}
