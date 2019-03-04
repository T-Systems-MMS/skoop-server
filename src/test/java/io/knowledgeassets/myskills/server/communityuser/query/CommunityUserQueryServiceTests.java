package io.knowledgeassets.myskills.server.communityuser.query;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserRepository;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.toList;

@ExtendWith(MockitoExtension.class)
class CommunityUserQueryServiceTests {

	@Mock
	private CommunityUserRepository communityUserRepository;

	private CommunityUserQueryService communityUserQueryService;

	@BeforeEach
	void setUp() {
		this.communityUserQueryService = new CommunityUserQueryService(communityUserRepository);
	}

	@DisplayName("Users of community are fetched.")
	@Test
	void communityUsersAreFetched() {

		given(communityUserRepository.findByCommunityId("123"))
				.willReturn(Stream.of(CommunityUser.builder()
								.id(123L)
								.role(CommunityRole.MANAGER)
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.build()
								)
								.user(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build())
								.build(),
						CommunityUser.builder()
								.id(456L)
								.role(CommunityRole.MEMBER)
								.community(Community.builder()
										.id("123")
										.title("Java User Group")
										.build())
								.user(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build())
								.build()
				));

		final Stream<CommunityUser> communityUserStream = communityUserQueryService.getCommunityUsers("123", null);
		final List<CommunityUser> communityUsers = communityUserStream.collect(toList());
		assertThat(communityUsers).containsExactly(
				CommunityUser.builder()
						.id(123L)
						.role(CommunityRole.MANAGER)
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build()
						)
						.user(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.build(),
				CommunityUser.builder()
						.id(456L)
						.role(CommunityRole.MEMBER)
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.build())
						.user(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.build()
		);
	}

}
