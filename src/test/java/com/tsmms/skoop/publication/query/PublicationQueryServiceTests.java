package com.tsmms.skoop.publication.query;

import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.publication.PublicationRepository;
import com.tsmms.skoop.skill.Skill;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PublicationQueryServiceTests {

	@Mock
	private PublicationRepository publicationRepository;

	private PublicationQueryService publicationQueryService;

	@BeforeEach
	void setUp() {
		this.publicationQueryService = new PublicationQueryService(publicationRepository);
	}

	@DisplayName("Gets user publications.")
	@Test
	void getsUserPublications() {

		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(publicationRepository.findByUserIdOrderByDateDesc("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Stream.of(
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
								.user(tester)
								.build(),
						Publication.builder()
								.id("456")
								.title("The second publication")
								.publisher("The second publisher")
								.date(LocalDate.of(2019, 4, 20))
								.link("http://second-link.com")
								.skills(new HashSet<>(Collections.singletonList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.user(tester)
								.build()
				));

		final List<Publication> publications = publicationQueryService.getUserPublications("56ef4778-a084-4509-9a3e-80b7895cf7b0").collect(Collectors.toList());
		Publication publication = publications.get(0);

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
		assertThat(publication.getUser()).isEqualTo(tester);

		publication = publications.get(1);

		assertThat(publication.getId()).isEqualTo("456");
		assertThat(publication.getTitle()).isEqualTo("The second publication");
		assertThat(publication.getPublisher()).isEqualTo("The second publisher");
		assertThat(publication.getDate()).isEqualTo(LocalDate.of(2019, 4, 20));
		assertThat(publication.getLink()).isEqualTo("http://second-link.com");
		assertThat(publication.getSkills()).containsExactlyInAnyOrder(Skill.builder()
				.id("123")
				.name("Java")
				.build());
		assertThat(publication.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 20, 13, 0));
		assertThat(publication.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 20, 13, 0));
		assertThat(publication.getUser()).isEqualTo(tester);
	}

	@DisplayName("Throws exception if user ID is null when getting user publications.")
	@Test
	void throwsExceptionIfUserIdIsNullWhenGettingUserPublications() {
		assertThrows(IllegalArgumentException.class, () -> publicationQueryService.getUserPublications(null));
	}

}
