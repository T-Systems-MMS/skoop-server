package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class CommunityRepositoryTests {

	@Autowired
	private CommunityRepository communityRepository;

	@Test
	@DisplayName("Provides the existing project queried by its exact name")
	void providesCommunityByExactName() {
		// Given
		communityRepository.save(
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
						.build()
		);
		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("Java User Group");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
		assertThat(community.get().getManagers()).hasSize(1);
		assertThat(community.get().getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.get().getMembers()).hasSize(1);
		assertThat(community.get().getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getMembers().get(0).getUserName()).isEqualTo("tester");
	}

	@Test
	@DisplayName("Provides the existing project queried by its name ignoring case")
	void providesCommunityByNameIgnoringCase() {
		// Given
		communityRepository.save(
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
						.build()
		);
		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("jaVA uSeR grOUp");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
		assertThat(community.get().getManagers()).hasSize(1);
		assertThat(community.get().getManagers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getManagers().get(0).getUserName()).isEqualTo("tester");
		assertThat(community.get().getMembers()).hasSize(1);
		assertThat(community.get().getMembers().get(0).getId()).isEqualTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community.get().getMembers().get(0).getUserName()).isEqualTo("tester");
	}

	@Test
	@DisplayName("Saves community with skills")
	void saveCommunityWithSkills() {
		// Given
		Community c = communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.description("Community for Java developers")
						.skills(Arrays.asList(Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
										.name("Angular")
										.build()))
						.build()
		);
		// When
		Optional<Community> community = communityRepository.findById(c.getId());
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPENED);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
		assertThat(community.get().getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
	}

}
