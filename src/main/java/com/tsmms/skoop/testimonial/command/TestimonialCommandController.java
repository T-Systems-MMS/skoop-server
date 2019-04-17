package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.util.Objects.requireNonNull;

@Api(tags = "Testimonials")
@RestController
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
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or project name exists"),
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
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final Testimonial testimonial = convertTestimonialRequestToTestimonial(request);
		testimonial.setUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(TestimonialResponse.of(testimonialCommandService.create(testimonial)));
	}

	private Testimonial convertTestimonialRequestToTestimonial(TestimonialRequest request) {
		return Testimonial.builder()
				.author(request.getAuthor())
				.comment(request.getComment())
				.skills(skillQueryService.skillNamesToSkills(request.getSkills()))
				.build();
	}

}
