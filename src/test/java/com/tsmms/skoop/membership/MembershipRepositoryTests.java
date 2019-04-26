package com.tsmms.skoop.membership;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class MembershipRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MembershipRepository membershipRepository;

	@DisplayName("Finds memberships by user ID.")
	@Test
	void findsMembershipsByUserId() {

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

		membershipRepository.saveAll(
				Arrays.asList(
						Membership.builder()
								.id("123")
								.name("First membership")
								.description("First membership description")
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
						Membership.builder()
								.id("456")
								.name("Second membership")
								.description("Second membership description")
								.link("http://second-link.com")
								.skills(new HashSet<>(Collections.singletonList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.user(anotherTester)
								.build(),
						Membership.builder()
								.id("789")
								.name("Third membership")
								.description("Third membership description")
								.link("http://third-link.com")
								.skills(new HashSet<>(Collections.singletonList(
										Skill.builder()
												.id("456")
												.name("Spring Boot")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2019, 4, 21, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 21, 13, 0))
								.user(tester)
								.build()
				)
		);

		final List<Membership> memberships = membershipRepository.findByUserIdOrderByDateDesc(tester.getId()).collect(Collectors.toList());
		Membership membership = memberships.get(0);
		assertThat(membership.getId()).isEqualTo("789");
		assertThat(membership.getName()).isEqualTo("Third membership");
		assertThat(membership.getDescription()).isEqualTo("Third membership description");
		assertThat(membership.getLink()).isEqualTo("http://third-link.com");
		assertThat(membership.getSkills()).containsExactlyInAnyOrder(Skill.builder()
				.id("456")
				.name("Spring Boot")
				.build());
		assertThat(membership.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 21, 13, 0));
		assertThat(membership.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 21, 13, 0));
		assertThat(membership.getUser()).isEqualTo(tester);

		membership = memberships.get(1);

		assertThat(membership.getId()).isEqualTo("123");
		assertThat(membership.getName()).isEqualTo("First membership");
		assertThat(membership.getDescription()).isEqualTo("First membership description");
		assertThat(membership.getLink()).isEqualTo("http://first-link.com");
		assertThat(membership.getSkills()).containsExactlyInAnyOrder(Skill.builder()
						.id("123")
						.name("Java")
						.build(),
				Skill.builder()
						.id("456")
						.name("Spring Boot")
						.build());
		assertThat(membership.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 19, 13, 0));
		assertThat(membership.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 19, 13, 0));
		assertThat(membership.getUser()).isEqualTo(tester);
	}

}
