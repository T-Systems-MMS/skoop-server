package com.tsmms.skoop.publication.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.publication.PublicationRepository;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class PublicationCommandServiceTests {

	@Mock
	private PublicationRepository publicationRepository;

	@Mock
	private SkillCommandService skillCommandService;

	private PublicationCommandService publicationCommandService;

	@BeforeEach
	void setUp() {
		publicationCommandService = new PublicationCommandService(publicationRepository, skillCommandService);
	}

	@DisplayName("Creates publication.")
	@Test
	void createsPublication() {

		given(skillCommandService.createNonExistentSkills(
				argThat(allOf(
						isA(Collection.class),
						containsInAnyOrder(Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						))
				))).willReturn(
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

		given(publicationRepository.save(
				argThat(allOf(
						isA(Publication.class),
						hasProperty("id", notNullValue()),
						hasProperty("title", is("The first publication")),
						hasProperty("date", equalTo(LocalDate.of(2019, 4, 19))),
						hasProperty("link", equalTo("http://first-link.com")),
						hasProperty("publisher", is("The first publisher")),
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
										.id("456")
										.name("Spring Boot")
										.build()
						))
				))
		)).willReturn(
				Publication.builder()
						.id("123")
						.title("The first publication")
						.publisher("The first publisher")
						.date(LocalDate.of(2019, 4, 19))
						.link("http://first-link.com")
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
						.creationDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		final Publication publication = publicationCommandService.create(
				Publication.builder()
						.title("The first publication")
						.publisher("The first publisher")
						.date(LocalDate.of(2019, 4, 19))
						.link("http://first-link.com")
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

		assertThat(publication.getId()).isEqualTo("123");
		assertThat(publication.getTitle()).isEqualTo("The first publication");
		assertThat(publication.getPublisher()).isEqualTo("The first publisher");
		assertThat(publication.getDate()).isEqualTo(LocalDate.of(2019, 4, 19));
		assertThat(publication.getLink()).isEqualTo("http://first-link.com");
		assertThat(publication.getSkills()).containsExactlyInAnyOrder(Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build());
		assertThat(publication.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 19, 13, 0));
		assertThat(publication.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 19, 13, 0));
		assertThat(publication.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

	@DisplayName("Updates publication.")
	@Test
	void updatePublication() {
		given(publicationRepository.findById("123")).willReturn(
				Optional.of(
						Publication.builder()
								.id("123")
								.title("The first publication")
								.publisher("The first publisher")
								.date(LocalDate.of(2019, 4, 19))
								.link("http://first-link.com")
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
								.creationDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.user(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
								.build()
				)
		);
		given(skillCommandService.createNonExistentSkills(
				argThat(
						allOf(
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
												.name("Angular")
												.build()
								)
						)
				))).willReturn(
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
		);

		given(publicationRepository.save(
				argThat(allOf(
						isA(Publication.class),
						hasProperty("id", notNullValue()),
						hasProperty("title", is("The first publication updated")),
						hasProperty("date", equalTo(LocalDate.of(2020, 4, 19))),
						hasProperty("link", equalTo("http://first-updated-link.com")),
						hasProperty("publisher", is("The first publisher updated")),
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
				Publication.builder()
						.id("123")
						.title("The first publication updated")
						.publisher("The first publisher updated")
						.date(LocalDate.of(2020, 4, 19))
						.link("http://first-updated-link.com")
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
						.creationDatetime(LocalDateTime.of(2019, 4, 22, 13, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 22, 13, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		final Publication publication = publicationCommandService.update("123", PublicationUpdateCommand.builder()
				.title("The first publication updated")
				.publisher("The first publisher updated")
				.date(LocalDate.of(2020, 4, 19))
				.link("http://first-updated-link.com")
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
										.name("Angular")
										.build()
						)
				))
				.build()
		);

		assertThat(publication.getId()).isEqualTo("123");
		assertThat(publication.getTitle()).isEqualTo("The first publication updated");
		assertThat(publication.getPublisher()).isEqualTo("The first publisher updated");
		assertThat(publication.getDate()).isEqualTo(LocalDate.of(2020, 4, 19));
		assertThat(publication.getLink()).isEqualTo("http://first-updated-link.com");
		assertThat(publication.getSkills()).containsExactlyInAnyOrder(Skill.builder()
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
		assertThat(publication.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 22, 13, 0));
		assertThat(publication.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 22, 13, 0));
		assertThat(publication.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

	@DisplayName("Throws exception if publication ID is null when updating publication.")
	@Test
	void throwsExceptionIfPublicationIdIsNullWhenUpdatingPublication() {
		assertThrows(IllegalArgumentException.class, () -> publicationCommandService.update(null, PublicationUpdateCommand.builder()
				.title("The first publication updated")
				.publisher("The first publisher updated")
				.date(LocalDate.of(2020, 4, 19))
				.link("http://first-updated-link.com")
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
										.name("Angular")
										.build()
						)
				))
				.build()
		));
	}

	@DisplayName("Throws exception if publication update command is null when updating publication.")
	@Test
	void throwsExceptionIfPublicationUpdateCommandIsNullWhenUpdatingPublication() {
		assertThrows(IllegalArgumentException.class, () -> publicationCommandService.update("123", null));
	}

	@DisplayName("Deletes publication.")
	@Test
	void deletesPublication() {
		given(publicationRepository.findById("123")).willReturn(Optional.of(
						Publication.builder()
								.id("123")
								.title("The first publication")
								.publisher("The first publisher")
								.date(LocalDate.of(2019, 4, 19))
								.link("http://first-link.com")
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
								.creationDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.user(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
								.build()
				)
		);
		assertDoesNotThrow(() -> publicationCommandService.delete("123"));
	}

	@DisplayName("Throws exception if publication ID is null when deleting publication.")
	@Test
	void throwsExceptionIfPublicationIdIsNullWhenDeletingPublication() {
		assertThrows(IllegalArgumentException.class, () -> publicationCommandService.delete(null));
	}

	@DisplayName("Throws exception if non existent publication is deleted.")
	@Test
	void throwsExceptionIfNonExistentPublicationIsDeleted() {
		given(publicationRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> publicationCommandService.delete("123"));
	}

}
