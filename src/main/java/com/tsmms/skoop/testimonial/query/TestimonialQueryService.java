package com.tsmms.skoop.testimonial.query;

import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.testimonial.TestimonialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class TestimonialQueryService {

	private final TestimonialRepository testimonialRepository;

	public TestimonialQueryService(TestimonialRepository testimonialRepository) {
		this.testimonialRepository = requireNonNull(testimonialRepository);
	}

	@Transactional(readOnly = true)
	public Stream<Testimonial> getUserTestimonials(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		return testimonialRepository.findByUserIdOrderByCreationDatetimeDesc(userId);
	}

}
