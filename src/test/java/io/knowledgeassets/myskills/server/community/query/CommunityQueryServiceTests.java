package io.knowledgeassets.myskills.server.community.query;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.community.RecommendedCommunity;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static java.util.stream.Collectors.toList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class CommunityQueryServiceTests {

	@Mock
	private CommunityRepository communityRepository;

	private CommunityQueryService communityQueryService;


	@BeforeEach
	void setUp() {
		communityQueryService = new CommunityQueryService(communityRepository);
	}

	@Test
	@DisplayName("Retrieves community by id")
	void retrievesCommunityById() {

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		given(communityRepository.findById("123")).willReturn(Optional.of(
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
						.skills(Arrays.asList(springBootSkill, angularSkill))
						.build()
		));
		final Optional<Community> community = communityQueryService.getCommunityById("123");
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.get().getLinks()).isEqualTo(Arrays.asList(
				Link.builder()
						.name("Facebook")
						.href("https://www.facebook.com/java-user-group")
						.build(),
				Link.builder()
						.name("Linkedin")
						.href("https://www.linkedin.com/java-user-group")
						.build()
		));
		assertThat(community.get().getManagers()).hasSize(1);
		assertThat(community.get().getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.get().getMembers()).hasSize(1);
		assertThat(community.get().getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getMembers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.get().getSkills()).hasSize(2);
		assertThat(community.get().getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
	}

	@Test
	@DisplayName("Returns a stream of all communities from the data repository")
	void returnsStreamOfCommunities() {

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill javascriptSkill = Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build();

		given(communityRepository.findAll()).willReturn(
				Arrays.asList(Community.builder()
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, angularSkill))
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPENED)
								.description("Community for Scala developers")
								.links(Arrays.asList(
										Link.builder()
												.name("Facebook")
												.href("https://www.facebook.com/scala-user-group")
												.build(),
										Link.builder()
												.name("Linkedin")
												.href("https://www.linkedin.com/scala-user-group")
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, javascriptSkill))
								.build())
		);

		Stream<Community> communities = communityQueryService.getCommunities();

		assertThat(communities).isNotNull();
		List<Community> communityList = communities.collect(toList());
		assertThat(communityList).hasSize(2);
		Community community = communityList.get(0);
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.getDescription()).isEqualTo("Community for Java developers");
		assertThat(community.getLinks()).isEqualTo(Arrays.asList(
				Link.builder()
						.name("Facebook")
						.href("https://www.facebook.com/java-user-group")
						.build(),
				Link.builder()
						.name("Linkedin")
						.href("https://www.linkedin.com/java-user-group")
						.build()
		));
		assertThat(community.getManagers()).hasSize(1);
		assertThat(community.getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.getMembers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
		community = communityList.get(1);
		assertThat(community.getId()).isEqualTo("456");
		assertThat(community.getTitle()).isEqualTo("Scala User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.getDescription()).isEqualTo("Community for Scala developers");
		assertThat(community.getLinks()).isEqualTo(Arrays.asList(
				Link.builder()
						.name("Facebook")
						.href("https://www.facebook.com/scala-user-group")
						.build(),
				Link.builder()
						.name("Linkedin")
						.href("https://www.linkedin.com/scala-user-group")
						.build()
		));
		assertThat(community.getManagers()).hasSize(1);
		assertThat(community.getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.getMembers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build());
	}

	@DisplayName("Test if communities recommended to a user are retrieved.")
	@Test
	void testIfRecommendedCommunitiesAreRetrieved() {

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill javascriptSkill = Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build();

		given(communityRepository.getRecommendedCommunities("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Stream.of(RecommendedCommunity.builder()
						.community(Community.builder()
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, angularSkill))
								.build())
						.recommended(true)
						.skillCounter(2L)
						.build(),
						RecommendedCommunity.builder()
						.community(Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPENED)
								.description("Community for Scala developers")
								.links(Arrays.asList(
										Link.builder()
												.name("Facebook")
												.href("https://www.facebook.com/scala-user-group")
												.build(),
										Link.builder()
												.name("Linkedin")
												.href("https://www.linkedin.com/scala-user-group")
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, javascriptSkill))
								.build())
						.skillCounter(0L)
						.recommended(false)
						.build()
				));

		Stream<RecommendedCommunity> recommendedCommunities = communityQueryService.getCommunitiesRecommendedForUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2");

		assertThat(recommendedCommunities).isNotNull();
		List<RecommendedCommunity> recommendedCommunityList = recommendedCommunities.collect(toList());
		assertThat(recommendedCommunityList).hasSize(2);
		RecommendedCommunity recommendedCommunity = recommendedCommunityList.get(0);
		assertThat(recommendedCommunity.getCommunity().getId()).isEqualTo("123");
		assertThat(recommendedCommunity.getCommunity().getTitle()).isEqualTo("Java User Group");
		assertThat(recommendedCommunity.getCommunity().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(recommendedCommunity.getCommunity().getDescription()).isEqualTo("Community for Java developers");
		assertThat(recommendedCommunity.getCommunity().getLinks()).isEqualTo(Arrays.asList(
				Link.builder()
						.name("Facebook")
						.href("https://www.facebook.com/java-user-group")
						.build(),
				Link.builder()
						.name("Linkedin")
						.href("https://www.linkedin.com/java-user-group")
						.build()
		));
		assertThat(recommendedCommunity.getCommunity().getManagers()).hasSize(1);
		assertThat(recommendedCommunity.getCommunity().getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(recommendedCommunity.getCommunity().getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(recommendedCommunity.getCommunity().getMembers()).hasSize(1);
		assertThat(recommendedCommunity.getCommunity().getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(recommendedCommunity.getCommunity().getMembers().get(0).getUserName()).isEqualTo("tester");
		assertThat(recommendedCommunity.getCommunity().getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
		assertThat(recommendedCommunity.isRecommended()).isTrue();
		assertThat(recommendedCommunity.getSkillCounter()).isEqualTo(2L);
		recommendedCommunity = recommendedCommunityList.get(1);
		assertThat(recommendedCommunity.getCommunity().getId()).isEqualTo("456");
		assertThat(recommendedCommunity.getCommunity().getTitle()).isEqualTo("Scala User Group");
		assertThat(recommendedCommunity.getCommunity().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(recommendedCommunity.getCommunity().getDescription()).isEqualTo("Community for Scala developers");
		assertThat(recommendedCommunity.getCommunity().getLinks()).isEqualTo(Arrays.asList(
				Link.builder()
						.name("Facebook")
						.href("https://www.facebook.com/scala-user-group")
						.build(),
				Link.builder()
						.name("Linkedin")
						.href("https://www.linkedin.com/scala-user-group")
						.build()
		));
		assertThat(recommendedCommunity.getCommunity().getManagers()).hasSize(1);
		assertThat(recommendedCommunity.getCommunity().getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(recommendedCommunity.getCommunity().getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(recommendedCommunity.getCommunity().getMembers()).hasSize(1);
		assertThat(recommendedCommunity.getCommunity().getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(recommendedCommunity.getCommunity().getMembers().get(0).getUserName()).isEqualTo("tester");
		assertThat(recommendedCommunity.getCommunity().getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build());
		assertThat(recommendedCommunity.getSkillCounter()).isEqualTo(0L);
	}

}
