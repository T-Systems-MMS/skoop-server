package io.knowledgeassets.myskills.server.community.query;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.Link;
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
		given(communityRepository.findById("123")).willReturn(Optional.of(
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
						.managers(singletonList(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build()))
						.members(singletonList(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build()))
						.build()
		));
		final Optional<Community> community = communityQueryService.getCommunityById("123");
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
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
	}

	@Test
	@DisplayName("Returns a stream of all communities from the data repository")
	void returnsStreamOfCommunities() {
		given(communityRepository.findAll()).willReturn(
				Arrays.asList(Community.builder()
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
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
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
								.build())
		);

		Stream<Community> communities = communityQueryService.getCommunities();

		assertThat(communities).isNotNull();
		List<Community> communityList = communities.collect(toList());
		assertThat(communityList).hasSize(2);
		Community community = communityList.get(0);
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
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
		community = communityList.get(1);
		assertThat(community.getId()).isEqualTo("456");
		assertThat(community.getTitle()).isEqualTo("Scala User Group");
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
	}

}
