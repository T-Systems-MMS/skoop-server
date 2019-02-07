package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommunityCommandServiceTests {

	@Mock
	private CommunityRepository communityRepository;

	private CommunityCommandService communityCommandService;

	@BeforeEach
	void setUp() {
		communityCommandService = new CommunityCommandService(communityRepository);
	}

	@Test
	@DisplayName("Tests if community is created.")
	void testIfCommunityIsCreated() {
		given(communityRepository.findByTitleIgnoreCase("Java User Group")).willReturn(Optional.empty());
		given(communityRepository.save(ArgumentMatchers.isA(Community.class)))
				.willReturn(
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

		Community community = communityCommandService.create(
				Community.builder()
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

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getDescription()).isEqualTo("Community for Java developers");
	}

	@Test
	@DisplayName("Create community throws exception when there is duplicate")
	void createCommunityThrowsExceptionWhenThereIsDuplicate() {
		given(communityRepository.findByTitleIgnoreCase("Java User Group")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Community for Java developers")
				.build()
				));
		assertThrows(DuplicateResourceException.class, () ->
			communityCommandService.create(
					Community.builder()
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
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Community for Java developers")
						.build()
		));
		given(communityRepository.save(ArgumentMatchers.isA(Community.class)))
				.willReturn(
						Community.builder()
								.id("123")
								.title("Java User Group")
								.description("New community for Java developers")
								.build()
				);

		Community community = communityCommandService.update(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("New community for Java developers")
						.build()
		);

		assertThat(community).isNotNull();
		assertThat(community.getId()).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getDescription()).isEqualTo("New community for Java developers");
	}

}
