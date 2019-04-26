package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.testimonial.TestimonialRequest;
import com.tsmms.skoop.testimonial.TestimonialResponse;
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

import static java.util.Objects.requireNonNull;
import static com.tsmms.skoop.exception.enums.Model.USER;

@Api(tags = "Testimonials")
@RestController
@Validated
public class TestimonialCommandController {

	private final TestimonialCommandService testimonialCommandService;
	private final UserQueryService userQueryService;
	private final SkillQueryService skillQueryService;

	public TestimonialCommandController(TestimonialCommandService testimonialCommandService,
										UserQueryService userQueryService,
										SkillQueryService skillQueryService) {
		this.testimonialCommandService = requireNonNull(testimonialCommandService);
		this.userQueryService = requireNonNull(userQueryService);
		this.skillQueryService = requireNonNull(skillQueryService);
	}

	@ApiOperation(value = "Create new user testimonial.",
			notes = "Create new user testimonial.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PostMapping(path = "/users/{userId}/testimonials",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestimonialResponse> createTestimonial(@PathVariable("userId") String userId,
															 @RequestBody @Valid TestimonialRequest request) {
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Testimonial testimonial = convertTestimonialRequestToTestimonial(request);
		testimonial.setUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(TestimonialResponse.of(testimonialCommandService.create(testimonial)));
	}

	@ApiOperation(value = "Update user testimonial.",
			notes = "Update user testimonial.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@PutMapping(path = "/users/{userId}/testimonials/{testimonialId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestimonialResponse> updateTestimonial(@PathVariable("userId") String userId,
																 @PathVariable("testimonialId") String testimonialId,
																 @RequestBody @Valid TestimonialRequest request) {
		final TestimonialUpdateCommand command = TestimonialUpdateCommand.builder()
				.author(request.getAuthor())
				.comment(request.getComment())
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(TestimonialResponse.of(testimonialCommandService.update(testimonialId, command)));
	}

	@ApiOperation(value = "Deletes user testimonial.",
			notes = "Deletes user testimonial.")
	@ApiResponses({
			@ApiResponse(code = 204, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data."),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@DeleteMapping(path = "/users/{userId}/testimonials/{testimonialId}")
	public ResponseEntity<Void> deleteTestimonial(@PathVariable("userId") String userId,
												  @PathVariable("testimonialId") String testimonialId) {
		testimonialCommandService.delete(testimonialId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private Testimonial convertTestimonialRequestToTestimonial(TestimonialRequest request) {
		return Testimonial.builder()
				.author(request.getAuthor())
				.comment(request.getComment())
				.skills(skillQueryService.convertSkillNamesToSkillsSet(request.getSkills()))
				.build();
	}

}
