package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.testimonial.TestimonialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class TestimonialCommandService {

	private final TestimonialRepository testimonialRepository;

	public TestimonialCommandService(TestimonialRepository testimonialRepository) {
		this.testimonialRepository = requireNonNull(testimonialRepository);
	}

	@Transactional
	public Testimonial create(Testimonial testimonial) {
		final LocalDateTime now = LocalDateTime.now();
		testimonial.setId(UUID.randomUUID().toString());
		testimonial.setCreationDatetime(now);
		testimonial.setLastModifiedDatetime(now);
		return testimonialRepository.save(testimonial);
	}

}
