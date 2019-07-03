package com.tsmms.skoop.membership.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.membership.MembershipRequest;
import com.tsmms.skoop.membership.MembershipResponse;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.tsmms.skoop.exception.enums.Model.USER;
import static java.util.Objects.requireNonNull;

@Api(tags = "Membership")
@RestController
@Validated
public class MembershipCommandController {

	private final MembershipCommandService membershipCommandService;
	private final SkillQueryService skillQueryService;
	private final UserQueryService userQueryService;

	public MembershipCommandController(MembershipCommandService membershipCommandService, SkillQueryService skillQueryService, UserQueryService userQueryService) {
		this.membershipCommandService = requireNonNull(membershipCommandService);
		this.skillQueryService = requireNonNull(skillQueryService);
		this.userQueryService = requireNonNull(userQueryService);
	}

	@ApiOperation(value = "Create new user membership.",
			notes = "Create new user membership.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PostMapping(path = "/users/{userId}/memberships",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MembershipResponse> createMembership(@PathVariable("userId") String userId,
																@RequestBody @Valid MembershipRequest request) {
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Membership membership = convertMembershipRequestToMembership(request);
		membership.setUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(MembershipResponse.of(membershipCommandService.create(membership)));
	}

	@ApiOperation(value = "Update user membership.",
			notes = "Update user membership.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/memberships/{membershipId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MembershipResponse> updateMembership(@PathVariable("userId") String userId,
																 @PathVariable("membershipId") String membershipId,
																 @RequestBody @Valid MembershipRequest request) {
		final MembershipUpdateCommand command = MembershipUpdateCommand.builder()
				.name(request.getName())
				.description(request.getDescription())
				.link(request.getLink())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(MembershipResponse.of(membershipCommandService.update(membershipId, command)));
	}

	@ApiOperation(value = "Deletes user membership.",
			notes = "Deletes user membership.")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@DeleteMapping(path = "/users/{userId}/memberships/{membershipId}")
	public ResponseEntity<Void> deleteMembership(@PathVariable("userId") String userId,
												  @PathVariable("membershipId") String membershipId) {
		membershipCommandService.delete(membershipId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private Membership convertMembershipRequestToMembership(MembershipRequest request) {
		return Membership.builder()
				.name(request.getName())
				.description(request.getDescription())
				.link(request.getLink())
				.startDate(request.getStartDate())
				.endDate(request.getEndDate())
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
	}

}
