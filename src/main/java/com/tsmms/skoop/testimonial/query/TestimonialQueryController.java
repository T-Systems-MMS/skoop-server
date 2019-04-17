package com.tsmms.skoop.testimonial.query;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.requireNonNull;

@Api(tags = "Testimonials")
@RestController
public class TestimonialQueryController {

	private final TestimonialQueryService testimonialQueryService;

	public TestimonialQueryController(TestimonialQueryService testimonialQueryService) {
		this.testimonialQueryService = requireNonNull(testimonialQueryService);
	}

}
