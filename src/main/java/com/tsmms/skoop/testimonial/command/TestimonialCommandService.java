package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.testimonial.TestimonialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static com.tsmms.skoop.exception.enums.Model.TESTIMONIAL;

@Service
public class TestimonialCommandService {

	private final TestimonialRepository testimonialRepository;
	private final SkillCommandService skillCommandService;

	public TestimonialCommandService(TestimonialRepository testimonialRepository,
									 SkillCommandService skillCommandService) {
		this.testimonialRepository = requireNonNull(testimonialRepository);
		this.skillCommandService = requireNonNull(skillCommandService);
	}

	@Transactional
	public Testimonial create(Testimonial testimonial) {
		final LocalDateTime now = LocalDateTime.now();
		testimonial.setId(UUID.randomUUID().toString());
		testimonial.setCreationDatetime(now);
		testimonial.setLastModifiedDatetime(now);
		testimonial.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(testimonial.getSkills())));
		return testimonialRepository.save(testimonial);
	}

	@Transactional
	public Testimonial update(String testimonialId, TestimonialUpdateCommand command) {
		if (testimonialId == null) {
			throw new IllegalArgumentException("Testimonial ID cannot be null.");
		}
		if (command == null) {
			throw new IllegalArgumentException("Command to update testimonial cannot be null.");
		}
		final Testimonial testimonial = testimonialRepository.findById(testimonialId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", testimonialId};
			return NoSuchResourceException.builder()
					.model(TESTIMONIAL)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		testimonial.setLastModifiedDatetime(LocalDateTime.now());
		testimonial.setAuthor(command.getAuthor());
		testimonial.setComment(command.getComment());
		testimonial.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(command.getSkills())));
		return testimonialRepository.save(testimonial);
	}

	@Transactional
	public void delete(String testimonialId) {
		if (testimonialId == null) {
			throw new IllegalArgumentException("Testimonial ID cannot be null.");
		}
		if (testimonialRepository.findById(testimonialId).isEmpty()) {
			final String[] searchParamsMap = {"id", testimonialId};
			throw NoSuchResourceException.builder()
					.model(TESTIMONIAL)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		testimonialRepository.deleteById(testimonialId);
	}

}
