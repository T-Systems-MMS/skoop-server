package io.knowledgeassets.myskills.server.community;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.Optional;

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
		);
		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("Java User Group");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
	}

	@Test
	@DisplayName("Provides the existing project queried by its name ignoring case")
	void providesCommunityByNameIgnoringCase() {
		// Given
		communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
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
		);
		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("jaVA uSeR grOUp");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
	}

}
