package com.tsmms.skoop.publication.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.publication.PublicationRequest;
import com.tsmms.skoop.publication.PublicationResponse;
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

@Api(tags = "Publications")
@RestController
@Validated
public class PublicationCommandController {

	private final UserQueryService userQueryService;
	private final SkillQueryService skillQueryService;
	private final PublicationCommandService publicationCommandService;

	public PublicationCommandController(UserQueryService userQueryService,
										SkillQueryService skillQueryService,
										PublicationCommandService publicationCommandService) {
		this.userQueryService = requireNonNull(userQueryService);
		this.skillQueryService = requireNonNull(skillQueryService);
		this.publicationCommandService = requireNonNull(publicationCommandService);
	}

	@ApiOperation(value = "Create new user publication.",
			notes = "Create new user publication.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PostMapping(path = "/users/{userId}/publications",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicationResponse> createPublication(@PathVariable("userId") String userId,
																 @RequestBody @Valid PublicationRequest request) {
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Publication publication = convertPublicationRequestToPublication(request);
		publication.setUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(PublicationResponse.of(publicationCommandService.create(publication)));
	}

	@ApiOperation(value = "Update user publication.",
			notes = "Update user publication.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/publications/{publicationId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicationResponse> updatePublication(@PathVariable("userId") String userId,
																 @PathVariable("publicationId") String publicationId,
																 @RequestBody @Valid PublicationRequest request) {
		final PublicationUpdateCommand command = PublicationUpdateCommand.builder()
				.title(request.getTitle())
				.date(request.getDate())
				.link(request.getLink())
				.publisher(request.getPublisher())
				.skills(skillQueryService.convertSkillNamesToSkills(request.getSkills()))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(PublicationResponse.of(publicationCommandService.update(publicationId, command)));
	}

	@ApiOperation(value = "Deletes user publication.",
			notes = "Deletes user publication.")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@DeleteMapping(path = "/users/{userId}/publications/{publicationId}")
	public ResponseEntity<Void> deletePublication(@PathVariable("userId") String userId,
												  @PathVariable("publicationId") String publicationId) {
		publicationCommandService.delete(publicationId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private Publication convertPublicationRequestToPublication(PublicationRequest request) {
		return Publication.builder()
				.title(request.getTitle())
				.date(request.getDate())
				.link(request.getLink())
				.publisher(request.getPublisher())
				.skills(skillQueryService.convertSkillNamesToSkills(request.getSkills()))
				.build();
	}

}
