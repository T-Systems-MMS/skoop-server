package com.tsmms.skoop.testimonial.query;

import com.tsmms.skoop.testimonial.TestimonialRepository;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class TestimonialQueryService {

	private final TestimonialRepository testimonialRepository;

	public TestimonialQueryService(TestimonialRepository testimonialRepository) {
		this.testimonialRepository = requireNonNull(testimonialRepository);
	}
}
