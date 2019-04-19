package com.tsmms.skoop.publication;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class PublicationRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PublicationRepository publicationRepository;

	@DisplayName("Finds publications by user ID.")
	@Test
	void findsPublicationsByUserId() {

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

		publicationRepository.saveAll(
				Arrays.asList(
						Publication.builder()
								.id("123")
								.title("The first publication")
								.publisher("The first publisher")
								.date(LocalDate.of(2019, 4, 19))
								.link("http://first-link.com")
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
								.skills(Collections.singletonList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build()
								))
								.creationDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.user(anotherTester)
								.build(),
						Publication.builder()
								.id("789")
								.title("The third publication")
								.publisher("The third publisher")
								.date(LocalDate.of(2019, 4, 21))
								.link("http://third-link.com")
								.skills(Collections.singletonList(
										Skill.builder()
												.id("456")
												.name("Spring Boot")
												.build()
								))
								.creationDatetime(LocalDateTime.of(2019, 4, 21, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 21, 13, 0))
								.user(tester)
								.build()
				)
		);

		final List<Publication> publications = publicationRepository.findByUserIdOrderByDateDesc(tester.getId()).collect(Collectors.toList());
		Publication publication = publications.get(0);
		assertThat(publication.getId()).isEqualTo("789");
		assertThat(publication.getTitle()).isEqualTo("The third publication");
		assertThat(publication.getPublisher()).isEqualTo("The third publisher");
		assertThat(publication.getDate()).isEqualTo(LocalDate.of(2019, 4, 21));
		assertThat(publication.getLink()).isEqualTo("http://third-link.com");
		assertThat(publication.getSkills()).containsExactlyInAnyOrder(Skill.builder()
				.id("456")
				.name("Spring Boot")
				.build());
		assertThat(publication.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 21, 13, 0));
		assertThat(publication.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 21, 13, 0));
		assertThat(publication.getUser()).isEqualTo(tester);

		publication = publications.get(1);

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
	}

}
