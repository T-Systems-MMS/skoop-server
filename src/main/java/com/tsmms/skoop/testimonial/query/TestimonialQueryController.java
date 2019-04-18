package com.tsmms.skoop.testimonial.query;

import com.tsmms.skoop.testimonial.TestimonialResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Api(tags = "Testimonials")
@RestController
public class TestimonialQueryController {

	private final TestimonialQueryService testimonialQueryService;

	public TestimonialQueryController(TestimonialQueryService testimonialQueryService) {
		this.testimonialQueryService = requireNonNull(testimonialQueryService);
	}

	@ApiOperation(value = "Gets user testimonials.",
			notes = "Gets user testimonials.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or project name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() and isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/testimonials", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TestimonialResponse>> getUserTestimonials(@PathVariable("userId") String userId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(testimonialQueryService.getUserTestimonials(userId).map(TestimonialResponse::of).collect(Collectors.toList()));
	}

}
