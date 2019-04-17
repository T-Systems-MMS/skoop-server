package com.tsmms.skoop.testimonial.command;

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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TestimonialCommandServiceTests {

	@Mock
	private TestimonialRepository testimonialRepository;

	private TestimonialCommandService testimonialCommandService;

	@BeforeEach
	void setUp() {
		testimonialCommandService = new TestimonialCommandService(testimonialRepository);
	}

	@DisplayName("Creates testimonial.")
	@Test
	void createTestimonial() {

		given(testimonialRepository.save(
				argThat(allOf(
						isA(Testimonial.class),
						hasProperty("id", notNullValue()),
						hasProperty("author", is("John Doe. Some company. CEO.")),
						hasProperty("comment", is("He is the best developer I have ever worked with.")),
						hasProperty("creationDatetime", isA(LocalDateTime.class)),
						hasProperty("lastModifiedDatetime", isA(LocalDateTime.class)),
						hasProperty("user", equalTo(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())),
						hasProperty("skills", contains(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						))
				))
		)).willReturn(
				Testimonial.builder()
						.id("abc")
						.author("John Doe. Some company. CEO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						))
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						))
						.creationDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		final Testimonial testimonial = testimonialCommandService.create(
				Testimonial.builder()
						.author("John Doe. Some company. CEO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		assertThat(testimonial.getAuthor()).isEqualTo("John Doe. Some company. CEO.");
		assertThat(testimonial.getComment()).isEqualTo("He is the best developer I have ever worked with.");
		assertThat(testimonial.getSkills()).containsExactlyInAnyOrder(Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build());
		assertThat(testimonial.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 10, 0));
		assertThat(testimonial.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 17, 10, 0));
		assertThat(testimonial.getId()).isEqualTo("abc");
		assertThat(testimonial.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

}
