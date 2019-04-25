package com.tsmms.skoop.testimonial.query;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.testimonial.TestimonialRepository;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TestimonialQueryServiceTests {

	@Mock
	private TestimonialRepository testimonialRepository;

	private TestimonialQueryService testimonialQueryService;

	@BeforeEach
	void setUp() {
		testimonialQueryService = new TestimonialQueryService(testimonialRepository);
	}

	@DisplayName("Gets user testimonials.")
	@Test
	void getsUserTestimonials() {
		given(testimonialRepository.findByUserIdOrderByCreationDatetimeDesc("56ef4778-a084-4509-9a3e-80b7895cf7b0")).willReturn(
				Stream.of(
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
								.user(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
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
								.user(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
								.build()
				)
		);
		final Stream<Testimonial> testimonialStream = testimonialQueryService.getUserTestimonials("56ef4778-a084-4509-9a3e-80b7895cf7b0");
		assertThat(testimonialStream).containsExactlyInAnyOrder(
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
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
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
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);
	}

	@DisplayName("Throws exception if user ID is null when getting user testimonials.")
	@Test
	void throwsExceptionIfUserIdIsNullWhenGettingUserTestimonials() {
		assertThrows(IllegalArgumentException.class, () -> testimonialQueryService.getUserTestimonials(null));
	}

}
