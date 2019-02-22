package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.mockito.BDDMockito.given;

import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class CommunityCommandServiceTests {

	@Mock
	private CommunityRepository communityRepository;

	@Mock
	private CurrentUserService currentUserService;

	@Mock
	private SkillCommandService skillCommandService;

	private CommunityCommandService communityCommandService;

	@BeforeEach
	void setUp() {
		communityCommandService = new CommunityCommandService(communityRepository, currentUserService, skillCommandService);
	}

	@Test
	@DisplayName("Tests if community is created.")
	void testIfCommunityIsCreated() {
		given(currentUserService.getCurrentUser()).willReturn(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		given(communityRepository.findByTitleIgnoreCase("Java User Group")).willReturn(Optional.empty());

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill tomcatSkill = Skill.builder()
				.name("Tomcat")
				.build();

		given(skillCommandService.createSkill("Tomcat", null, null))
				.willReturn(
						Skill.builder()
								.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
								.name("Tomcat")
								.build()
				);

		given(communityRepository.save(argThat(allOf(
				isA(Community.class),
				hasProperty("title", is("Java User Group")),
				hasProperty("type", is(CommunityType.OPENED)),
				hasProperty("description", is("Community for Java developers")),
				hasProperty("links", hasItems(Link.builder()
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
								.type(CommunityType.OPENED)
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
								.managers(singletonList(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()))
								.members(singletonList(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()))
								.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
										.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
										.name("Tomcat")
										.build()))
								.build()
				);

		Community community = communityCommandService.create(
				Community.builder()
						.title("Java User Group")
						.type(CommunityType.OPENED)
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
						.skills(Arrays.asList(springBootSkill, angularSkill, tomcatSkill))
						.build()
		);

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.getDescription()).isEqualTo("Community for Java developers");
		assertThat(community.getManagers()).hasSize(1);
		assertThat(community.getManagers().get(0)).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers().get(0)).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		assertThat(community.getSkills()).contains(
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
						.type(CommunityType.OPENED)
						.description("Community for Java developers")
						.build()
		));
		assertThrows(DuplicateResourceException.class, () ->
				communityCommandService.create(
						Community.builder()
								.title("Java User Group")
								.type(CommunityType.OPENED)
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
								.build()
				));
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
	@DisplayName("Tests if community is updated")
	void testIfCommunityIsUpdated() {

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill tomcatSkill = Skill.builder()
				.name("Tomcat")
				.build();

		given(skillCommandService.createSkill("Tomcat", null, null))
				.willReturn(
						Skill.builder()
								.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
								.name("Tomcat")
								.build()
				);

		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.description("Community for Java developers")
						.managers(singletonList(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build()))
						.members(singletonList(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build()))
						.skills(Arrays.asList(springBootSkill, angularSkill))
						.build()
		));
		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("title", is("Java User Group")),
						hasProperty("type", is(CommunityType.OPENED)),
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
						))
				))
		))
				.willReturn(
						Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPENED)
								.description("New community for Java developers")
								.managers(singletonList(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()))
								.members(singletonList(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()))
								.skills(Arrays.asList(springBootSkill, angularSkill, Skill.builder()
										.id("a3d55d3f-1215-4e8e-93f3-c06a5b9c2d56")
										.name("Tomcat")
										.build()))
								.build()
				);

		Community community = communityCommandService.update(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.description("New community for Java developers")
						.skills(Arrays.asList(springBootSkill, angularSkill, tomcatSkill))
						.build()
		);

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.getDescription()).isEqualTo("New community for Java developers");
		assertThat(community.getManagers()).hasSize(1);
		assertThat(community.getManagers().get(0)).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers().get(0)).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
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
	}

}
