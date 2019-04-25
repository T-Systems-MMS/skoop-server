package com.tsmms.skoop.testimonial;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class TestimonialRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TestimonialRepository testimonialRepository;

	@DisplayName("Finds testimonials by user id.")
	@Test
	void findsTestimonialsByUserId() {

		final User tester = userRepository.save(
				User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.build()
		);

		final User anotherTester = userRepository.save(
				User.builder()
						.id("c9cf7118-5f9e-40fc-9d89-28b2d0a77340")
						.userName("anotherTester")
						.build()
		);

		testimonialRepository.saveAll(Arrays.asList(
				Testimonial.builder()
						.id("abc")
						.author("John Doe. Some company. CEO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						)))
						.creationDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.user(tester)
						.build(),
				Testimonial.builder()
						.id("xyz")
						.author("Jim Doe. Some company. CTO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						)))
						.creationDatetime(LocalDateTime.of(2019, 4, 17, 11, 30))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 17, 11, 30))
						.user(tester)
						.build(),
				Testimonial.builder()
						.id("def")
						.author("Jenny Doe. Another company. CEO.")
						.comment("He is one of the best developers I have ever worked with.")
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("789")
										.name("JavaScript")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						)))
						.creationDatetime(LocalDateTime.of(2018, 4, 17, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2018, 4, 17, 10, 0))
						.user(anotherTester)
						.build()
				)
		);

		final List<Testimonial> testimonials = testimonialRepository.findByUserIdOrderByCreationDatetimeDesc(tester.getId()).collect(Collectors.toList());
		assertThat(testimonials).hasSize(2);

		Testimonial testimonial = testimonials.get(0);
		assertThat(testimonial.getId()).isEqualTo("xyz");
		assertThat(testimonial.getAuthor()).isEqualTo("Jim Doe. Some company. CTO.");
		assertThat(testimonial.getComment()).isEqualTo("He is the best developer I have ever worked with.");
		assertThat(testimonial.getSkills()).containsExactlyInAnyOrder(
				Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build()
		);
		assertThat(testimonial.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 11, 30));
		assertThat(testimonial.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 11, 30));
		assertThat(testimonial.getUser()).isEqualTo(tester);

		testimonial = testimonials.get(1);
		assertThat(testimonial.getId()).isEqualTo("abc");
		assertThat(testimonial.getAuthor()).isEqualTo("John Doe. Some company. CEO.");
		assertThat(testimonial.getComment()).isEqualTo("He is the best developer I have ever worked with.");
		assertThat(testimonial.getSkills()).containsExactlyInAnyOrder(
				Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build()
		);
		assertThat(testimonial.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 10, 0));
		assertThat(testimonial.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 10, 0));
		assertThat(testimonial.getUser()).isEqualTo(tester);
	}

}
