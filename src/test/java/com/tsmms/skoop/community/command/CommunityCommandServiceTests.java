package com.tsmms.skoop.community.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.community.link.command.LinkCommandService;
import com.tsmms.skoop.communityuser.registration.query.CommunityUserRegistrationQueryService;
import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.command.CommunityUserCommandService;
import com.tsmms.skoop.communityuser.query.CommunityUserQueryService;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.communityuser.registration.command.CommunityUserRegistrationCommandService;
import com.tsmms.skoop.user.query.UserQueryService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommunityCommandServiceTests {

	@Mock
	private CommunityRepository communityRepository;

	@Mock
	private CurrentUserService currentUserService;

	@Mock
	private SkillCommandService skillCommandService;

	@Mock
	private CommunityUserCommandService communityUserCommandService;

	@Mock
	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@Mock
	private CommunityUserQueryService communityUserQueryService;

	@Mock
	private NotificationCommandService notificationCommandService;

	@Mock
	private LinkCommandService linkCommandService;

	@Mock
	private CommunityUserRegistrationQueryService communityUserRegistrationQueryService;

	@Mock
	private UserQueryService userQueryService;

	private CommunityCommandService communityCommandService;

	@BeforeEach
	void setUp() {
		communityCommandService = new CommunityCommandService(communityRepository,
				currentUserService,
				skillCommandService,
				communityUserRegistrationCommandService,
				communityUserCommandService,
				communityUserQueryService,
				notificationCommandService,
				linkCommandService,
				communityUserRegistrationQueryService,
				userQueryService);
	}

	@Test
	@DisplayName("Tests if community is created.")
	void testIfCommunityIsCreated() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(currentUserService.getCurrentUserId()).willReturn("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		given(communityRepository.findByTitleIgnoreCase("Java User Group")).willReturn(Optional.empty());

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		given(skillCommandService.createNonExistentSkills(Arrays.asList(
				springBootSkill, angularSkill, Skill.builder()
						.name("Tomcat")
						.build()
		))).willReturn(
				Arrays.asList(springBootSkill, angularSkill, Skill.builder()
						.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
						.name("Tomcat")
						.build())
		);

		given(communityRepository.save(argThat(AllOf.allOf(
				isA(Community.class),
				hasProperty("title", is("Java User Group")),
				hasProperty("type", Matchers.is(CommunityType.OPEN)),
				hasProperty("description", is("Community for Java developers")),
				hasProperty("links", Matchers.hasItems(Link.builder()
								.name("Facebook")
								.href("https://www.facebook.com/java-user-group")
								.build(),
						Link.builder()
								.name("Linkedin")
								.href("https://www.linkedin.com/java-user-group")
								.build()
				)),
				hasProperty("skills", hasItems(
						Skill.builder()
								.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
								.name("Angular")
								.build(),
						Skill.builder()
								.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
								.name("Tomcat")
								.build()
				))
		))))
				.willReturn(
						Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.description("Community for Java developers")
								.links(Arrays.asList(
										Link.builder()
												.name("Facebook")
												.href("https://www.facebook.com/java-user-group")
												.build(),
										Link.builder()
												.name("Linkedin")
												.href("https://www.linkedin.com/java-user-group")
												.build()
								))
								.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
										.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
										.name("Tomcat")
										.build()))
								.build()
				);

		Community community = communityCommandService.create(
				Community.builder()
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.description("Community for Java developers")
						.links(Arrays.asList(
								Link.builder()
										.name("Facebook")
										.href("https://www.facebook.com/java-user-group")
										.build(),
								Link.builder()
										.name("Linkedin")
										.href("https://www.linkedin.com/java-user-group")
										.build()
						))
						.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
								.name("Tomcat")
								.build()))
						.build(),
				Arrays.asList(
						User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("firstTester")
								.build(),
						User.builder()
								.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
								.userName("secondTester")
								.build()
				)
		);

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPEN);
		assertThat(community.getDescription()).isEqualTo("Community for Java developers");
		Assertions.assertThat(community.getSkills()).contains(
				Skill.builder()
						.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
						.name("Angular")
						.build(),
				Skill.builder()
						.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
						.name("Tomcat")
						.build());
	}

	@Test
	@DisplayName("Create community throws exception when there is duplicate")
	void createCommunityThrowsExceptionWhenThereIsDuplicate() {
		given(communityRepository.findByTitleIgnoreCase("Java User Group")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.description("Community for Java developers")
						.build()
		));
		assertThrows(DuplicateResourceException.class, () ->
				communityCommandService.create(
						Community.builder()
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.description("Community for Java developers")
								.links(Arrays.asList(
										Link.builder()
												.name("Facebook")
												.href("https://www.facebook.com/java-user-group")
												.build(),
										Link.builder()
												.name("Linkedin")
												.href("https://www.linkedin.com/java-user-group")
												.build()
								))
								.build(),
						Arrays.asList(
								User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("firstTester")
										.build(),
								User.builder()
										.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
										.userName("secondTester")
										.build()
						)
				));
	}

	@DisplayName("Delete community.")
	@Test
	void deleteCommunity() {
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build()
		));
		given(communityUserQueryService.getCommunityUsers("123", null))
				.willReturn(
						Stream.of(
								CommunityUser.builder()
										.role(CommunityRole.MEMBER)
										.user(
												User.builder()
														.id("abc")
														.userName("tester")
														.build()
										)
										.community(
												Community.builder()
														.id("123")
														.title("Java User Group")
														.type(CommunityType.CLOSED)
														.build()
										)
										.id(1L)
										.creationDate(LocalDateTime.of(2019, 3, 10, 9, 0))
										.lastModifiedDate(LocalDateTime.of(2019, 3, 10, 9, 0))
										.build()
						)
				);
		assertDoesNotThrow(() -> communityCommandService.delete("123"));
	}

	@Test
	@DisplayName("Delete community throws an exception when there is no such a community")
	void deleteCommunityThrowsExceptionWhenThereIsNoSuchCommunity() {
		given(communityRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> communityCommandService.delete("123"));
	}

	@Test
	@DisplayName("Update community throws an exception when there is no such a community")
	void updateCommunityThrowsExceptionWhenThereIsNoSuchCommunity() {
		given(communityRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> communityCommandService.update(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Community for Java developers")
						.build()
		));
	}

	@Test
	@DisplayName("Update community.")
	void updateCommunity() {

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		given(skillCommandService.createNonExistentSkills(Arrays.asList(
				springBootSkill, angularSkill, Skill.builder()
						.name("Tomcat")
						.build()
		))).willReturn(
				Arrays.asList(springBootSkill, angularSkill, Skill.builder()
						.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
						.name("Tomcat")
						.build())
		);

		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.description("Community for Java developers")
						.skills(Arrays.asList(springBootSkill, angularSkill))
						.links(Collections.singletonList(Link.builder()
								.name("jira")
								.href("https://www.oldjira.com")
								.build()
						))
						.build()
		));
		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("title", is("New java User Group")),
						hasProperty("type", is(CommunityType.CLOSED)),
						hasProperty("description", is("New community for Java developers")),
						hasProperty("skills", hasItems(
								Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
										.name("Angular")
										.build(),
								Skill.builder()
										.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
										.name("Tomcat")
										.build()
						)),
						hasProperty("links", hasItems(
								Link.builder()
										.name("jira")
										.href("https://www.newjira.com")
										.build()
						))
				))
		))
				.willReturn(
						Community.builder()
								.id("123")
								.title("New java User Group")
								.type(CommunityType.CLOSED)
								.description("New community for Java developers")
								.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
										.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
										.name("Tomcat")
										.build()))
								.links(Collections.singletonList(Link.builder()
										.id(12L)
										.name("jira")
										.href("https://www.newjira.com")
										.build()
								))
								.build()
				);

		Community community = communityCommandService.update(
				Community.builder()
						.id("123")
						.title("New java User Group")
						.type(CommunityType.CLOSED)
						.description("New community for Java developers")
						.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
								.name("Tomcat")
								.build()))
						.links(Collections.singletonList(Link.builder()
								.name("jira")
								.href("https://www.newjira.com")
								.build()
						))
						.build()
		);

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("New java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.CLOSED);
		assertThat(community.getDescription()).isEqualTo("New community for Java developers");
		assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build(), Skill.builder()
				.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
				.name("Tomcat")
				.build());
		assertThat(community.getLinks()).contains(Link.builder()
				.id(12L)
				.name("jira")
				.href("https://www.newjira.com")
				.build());
	}

}
