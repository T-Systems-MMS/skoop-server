package com.tsmms.skoop.membership.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.membership.MembershipRepository;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class MembershipCommandServiceTests {

	@Mock
	private SkillCommandService skillCommandService;

	@Mock
	private MembershipRepository membershipRepository;

	private MembershipCommandService membershipCommandService;

	@BeforeEach
	void setUp() {
		this.membershipCommandService = new MembershipCommandService(skillCommandService, membershipRepository);
	}

	@DisplayName("Creates membership.")
	@Test
	void createsMembership() {

		given(skillCommandService.createNonExistentSkills(argThat(allOf(
				isA(Collection.class),
				containsInAnyOrder(
				Skill.builder()
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

		given(membershipRepository.save(
				argThat(allOf(
						isA(Membership.class),
						hasProperty("id", notNullValue()),
						hasProperty("name", is("First membership")),
						hasProperty("description", is("First membership description")),
						hasProperty("link", equalTo("http://first-link.com")),
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
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
		);

		final Membership membership = membershipCommandService.create(
				Membership.builder()
						.name("First membership")
						.description("First membership description")
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
		assertThat(membership.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

	@DisplayName("Updates membership.")
	@Test
	void updateMembership() {
		given(membershipRepository.findById("123")).willReturn(
				Optional.of(
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
								.user(User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("tester")
										.build())
								.build()
				)
		);
		given(skillCommandService.createNonExistentSkills(argThat(allOf(
				isA(Collection.class),
				hasItems(
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
		)))).willReturn(
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

		given(membershipRepository.save(
				argThat(allOf(
						isA(Membership.class),
						hasProperty("id", notNullValue()),
						hasProperty("name", is("First membership updated")),
						hasProperty("description", is("First membership description updated")),
						hasProperty("link", equalTo("http://first-updated-link.com")),
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
				Membership.builder()
						.id("123")
						.name("First membership updated")
						.description("First membership description updated")
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

		final Membership membership = membershipCommandService.update("123", MembershipUpdateCommand.builder()
				.name("First membership updated")
				.description("First membership description updated")
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

		assertThat(membership.getId()).isEqualTo("123");
		assertThat(membership.getName()).isEqualTo("First membership updated");
		assertThat(membership.getDescription()).isEqualTo("First membership description updated");
		assertThat(membership.getLink()).isEqualTo("http://first-updated-link.com");
		assertThat(membership.getSkills()).containsExactlyInAnyOrder(Skill.builder()
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
		assertThat(membership.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 22, 13, 0));
		assertThat(membership.getLastModifiedDatetime()).isEqualTo(LocalDateTime.of(2019, 4, 22, 13, 0));
		assertThat(membership.getUser()).isEqualTo(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build());
	}

	@DisplayName("Throws exception if membership ID is null when updating membership.")
	@Test
	void throwsExceptionIfMembershipIdIsNullWhenUpdatingMembership() {
		assertThrows(IllegalArgumentException.class, () -> membershipCommandService.update(null, MembershipUpdateCommand.builder()
				.name("First membership updated")
				.description("First membership description updated")
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

	@DisplayName("Throws exception if membership update command is null when updating membership.")
	@Test
	void throwsExceptionIfMembershipUpdateCommandIsNullWhenUpdatingMembership() {
		assertThrows(IllegalArgumentException.class, () -> membershipCommandService.update("123", null));
	}

	@DisplayName("Deletes membership.")
	@Test
	void deletesMembership() {
		given(membershipRepository.findById("123")).willReturn(Optional.of(
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
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
				)
		);
		assertDoesNotThrow(() -> membershipCommandService.delete("123"));
	}

	@DisplayName("Throws exception if membership ID is null when deleting membership.")
	@Test
	void throwsExceptionIfMembershipIdIsNullWhenDeletingMembership() {
		assertThrows(IllegalArgumentException.class, () -> membershipCommandService.delete(null));
	}

	@DisplayName("Throws exception if non existent membership is deleted.")
	@Test
	void throwsExceptionIfNonExistentMembershipIsDeleted() {
		given(membershipRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> membershipCommandService.delete("123"));
	}

}
