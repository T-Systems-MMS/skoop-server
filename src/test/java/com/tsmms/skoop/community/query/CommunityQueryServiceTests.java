package com.tsmms.skoop.community.query;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.skill.Skill;
import org.assertj.core.api.Assertions;
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
						.skills(Arrays.asList(springBootSkill, angularSkill))
						.build()
		));
		final Optional<Community> community = communityQueryService.getCommunityById("123");
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPEN);
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
		Assertions.assertThat(community.get().getSkills()).hasSize(2);
		Assertions.assertThat(community.get().getSkills()).contains(Skill.builder()
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, angularSkill))
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPEN)
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
		assertThat(community.getType()).isEqualTo(CommunityType.OPEN);
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
		Assertions.assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
		community = communityList.get(1);
		assertThat(community.getId()).isEqualTo("456");
		assertThat(community.getTitle()).isEqualTo("Scala User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPEN);
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
		Assertions.assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build());
	}

	@DisplayName("Communities recommended to a user are retrieved.")
	@Test
	void recommendedCommunitiesAreRetrieved() {

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
				Stream.of(Community.builder()
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, angularSkill))
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPEN)
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, javascriptSkill))
								.build()
				));

		Stream<Community> communities = communityQueryService.getCommunitiesRecommendedForUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2");

		assertThat(communities).isNotNull();
		List<Community> communityList = communities.collect(toList());
		assertThat(communityList).hasSize(2);
		Community community = communityList.get(0);
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPEN);
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
		Assertions.assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
		community = communityList.get(1);
		assertThat(community.getId()).isEqualTo("456");
		assertThat(community.getTitle()).isEqualTo("Scala User Group");
		assertThat(community.getType()).isEqualTo(CommunityType.OPEN);
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
		Assertions.assertThat(community.getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build());
	}

	@DisplayName("Is user a community member?")
	@Test
	void isCommunityMember() {
		given(communityRepository.isCommunityMember("123","456")).willReturn(true);
		assertThat(communityQueryService.isCommunityMember("123", "456")).isTrue();
	}

	@DisplayName("User communities are retrieved.")
	@Test
	void userCommunitiesAreRetrieved() {
		given(communityRepository.getUserCommunities("123")).willReturn(Stream.of(
				Community.builder()
				.id("abc")
				.title("Java User Group")
				.build(),
				Community.builder()
				.id("efd")
				.title("Scala User Group")
				.build()
		));
		final Stream<Community> communityStream = communityQueryService.getUserCommunities("123");
		final List<Community> communities = communityStream.collect(toList());
		assertThat(communities).contains(
				Community.builder()
						.id("abc")
						.title("Java User Group")
						.build(),
				Community.builder()
						.id("efd")
						.title("Scala User Group")
						.build()
		);
	}

	@DisplayName("User has community manager role.")
	@Test
	void userHasCommunityManagerRole() {
		given(communityRepository.isCommunityManager("123", "456")).willReturn(true);
		assertThat(communityQueryService.isCommunityManager("123", "456")).isTrue();
	}

}
