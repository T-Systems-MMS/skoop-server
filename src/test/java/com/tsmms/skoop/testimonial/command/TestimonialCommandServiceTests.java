package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TestimonialCommandServiceTests {

	@Mock
	private TestimonialRepository testimonialRepository;

	@Mock
	private SkillCommandService skillCommandService;

	private TestimonialCommandService testimonialCommandService;

	@BeforeEach
	void setUp() {
		testimonialCommandService = new TestimonialCommandService(testimonialRepository, skillCommandService);
	}

	@DisplayName("Creates testimonial.")
	@Test
	void createTestimonial() {
		given(skillCommandService.createNonExistentSkills(argThat(
				allOf(
						isA(Collection.class),
						containsInAnyOrder(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						)
				))))
				.willReturn(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						)
				);
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
						hasProperty("skills", containsInAnyOrder(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						))
				))
		)).willReturn(
				Testimonial.builder()
						.id("abc")
						.author("John Doe. Some company. CEO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(new HashSet<>(
								Arrays.asList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build(),
										Skill.builder()
												.id("456")
												.name("Spring Boot")
												.build()
								)
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
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						)))
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

	@DisplayName("Updates testimonial.")
	@Test
	void updatesTestimonial() {
		given(skillCommandService.createNonExistentSkills(argThat(allOf(
				isA(Collection.class),
				containsInAnyOrder(
						Skill.builder()
								.id("123")
								.name("Java")
								.build(),
						Skill.builder()
								.id("456")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.id("789")
								.name("Angular")
								.build()
				)
		)))).willReturn(Arrays.asList(
				Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.id("789")
						.name("Angular")
						.build()
		));
		given(testimonialRepository.findById("abc"))
				.willReturn(
						Optional.of(
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
										.build()
						)
				);

		given(testimonialRepository.save(
				argThat(allOf(
						isA(Testimonial.class),
						hasProperty("id", notNullValue()),
						hasProperty("author", is("John Doe. Another company. CTO.")),
						hasProperty("comment", is("He is one of the best developers I have ever worked with.")),
						hasProperty("creationDatetime", isA(LocalDateTime.class)),
						hasProperty("lastModifiedDatetime", isA(LocalDateTime.class)),
						hasProperty("user", equalTo(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())),
						hasProperty("skills", containsInAnyOrder(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						))
				))
		)).willReturn(
				Testimonial.builder()
						.id("abc")
						.author("John Doe. Another company. CTO.")
						.comment("He is one of the best developers I have ever worked with.")
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						)))
						.creationDatetime(LocalDateTime.of(2019, 4, 18, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 18, 10, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		final Testimonial testimonial = testimonialCommandService.update("abc", TestimonialUpdateCommand.builder()
				.author("John Doe. Another company. CTO.")
				.comment("He is one of the best developers I have ever worked with.")
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						)
				))
				.build()
		);
		assertThat(testimonial.getAuthor()).isEqualTo("John Doe. Another company. CTO.");
		assertThat(testimonial.getComment()).isEqualTo("He is one of the best developers I have ever worked with.");
		assertThat(testimonial.getSkills()).containsExactlyInAnyOrder(Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.id("789")
						.name("Angular")
						.build());
		assertThat(testimonial.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 18, 10, 0));
		assertThat(testimonial.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 18, 10, 0));
		assertThat(testimonial.getId()).isEqualTo("abc");
		assertThat(testimonial.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

	@DisplayName("Throws exception if testimonial ID is null when updating testimonial.")
	@Test
	void throwsExceptionIfTestimonialIdIsNullWhenUpdatingTestimonial() {
		assertThrows(IllegalArgumentException.class, () -> testimonialCommandService.update(null, TestimonialUpdateCommand.builder()
				.author("John Doe. Another company. CTO.")
				.comment("He is one of the best developers I have ever worked with.")
				.skills(new HashSet<>(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						)
				))
				.build()
		));
	}

	@DisplayName("Throws exception if testimonial update command is null when updating testimonial.")
	@Test
	void throwsExceptionIfTestimonialUpdateCommandIsNullWhenUpdatingTestimonial() {
		assertThrows(IllegalArgumentException.class, () -> testimonialCommandService.update("123", null));
	}

	@DisplayName("Deletes testimonial.")
	@Test
	void deletesTestimonial() {
		given(testimonialRepository.findById("123")).willReturn(Optional.of(
				Testimonial.builder()
						.id("123")
						.author("John Doe. Another company. CTO.")
						.comment("He is one of the best developers I have ever worked with.")
						.skills(new HashSet<>(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						)))
						.creationDatetime(LocalDateTime.of(2019, 4, 18, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 18, 10, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		));
		assertDoesNotThrow(() -> testimonialCommandService.delete("123"));
	}

	@DisplayName("Throws exception if testimonial ID is null when deleting testimonial.")
	@Test
	void throwsExceptionIfTestimonialIdIsNullWhenDeletingTestimonial() {
		assertThrows(IllegalArgumentException.class, () -> testimonialCommandService.delete(null));
	}

	@DisplayName("Throws exception if non existent testimonial is deleted.")
	@Test
	void throwsExceptionIfNonExistentTestimonialIsDeleted() {
		given(testimonialRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> testimonialCommandService.delete("123"));
	}

}
