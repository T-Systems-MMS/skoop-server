package com.tsmms.skoop.membership.query;

import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.membership.MembershipRepository;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MembershipQueryServiceTests {

	@Mock
	private MembershipRepository membershipRepository;

	private MembershipQueryService membershipQueryService;

	@BeforeEach
	void setUp() {
		this.membershipQueryService = new MembershipQueryService(membershipRepository);
	}

	@DisplayName("Gets user memberships.")
	@Test
	void getsUserMemberships() {

		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(membershipRepository.findByUserIdOrderByDateDesc("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Stream.of(
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
								.user(tester)
								.build()
				));

		final List<Membership> memberships = membershipQueryService.getUserMemberships("56ef4778-a084-4509-9a3e-80b7895cf7b0").collect(Collectors.toList());
		Membership membership = memberships.get(0);

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

		membership = memberships.get(1);

		assertThat(membership.getId()).isEqualTo("456");
		assertThat(membership.getName()).isEqualTo("Second membership");
		assertThat(membership.getDescription()).isEqualTo("Second membership description");
		assertThat(membership.getLink()).isEqualTo("http://second-link.com");
		assertThat(membership.getSkills()).containsExactlyInAnyOrder(Skill.builder()
				.id("123")
				.name("Java")
				.build());
		assertThat(membership.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 20, 13, 0));
		assertThat(membership.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 20, 13, 0));
		assertThat(membership.getUser()).isEqualTo(tester);
	}

	@DisplayName("Throws exception if user ID is null when getting user memberships.")
	@Test
	void throwsExceptionIfUserIdIsNullWhenGettingUserMemberships() {
		assertThrows(IllegalArgumentException.class, () -> membershipQueryService.getUserMemberships(null));
	}

}
