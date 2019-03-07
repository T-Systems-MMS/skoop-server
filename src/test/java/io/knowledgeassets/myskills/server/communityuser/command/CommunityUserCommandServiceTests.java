package io.knowledgeassets.myskills.server.communityuser.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserRepository;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.user.User;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class CommunityUserCommandServiceTests {

	@Mock
	private CommunityUserRepository communityUserRepository;

	private CommunityUserCommandService communityUserCommandService;

	@BeforeEach
	void setUp() {
		this.communityUserCommandService = new CommunityUserCommandService(communityUserRepository);
	}

	@Test
	@DisplayName("User joins community.")
	void userJoinsCommunity() {

		given(communityUserRepository.save(
				argThat(allOf(
						isA(CommunityUser.class),
						hasProperty("community", equalTo(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.build())),
						hasProperty("user", equalTo(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())),
						hasProperty("role", is(CommunityRole.MEMBER)),
						hasProperty("creationDate", isA(LocalDateTime.class)),
						hasProperty("lastModifiedDate", isA(LocalDateTime.class))
				)))
		).willReturn(CommunityUser.builder()
				.community(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build())
				.user(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build())
				.id(123L)
				.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.role(CommunityRole.MEMBER)
				.build()
		);

		final CommunityUser communityUser = communityUserCommandService.create(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.build(),
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				CommunityRole.MEMBER);
		assertThat(communityUser).isNotNull();
		assertThat(communityUser.getId()).isEqualTo(123L);
		assertThat(communityUser.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.build());
		assertThat(communityUser.getUser()).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		assertThat(communityUser.getRole()).isEqualTo(CommunityRole.MEMBER);
		assertThat(communityUser.getCreationDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(communityUser.getLastModifiedDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
	}

	@DisplayName("User leaves community")
	@Test
	void userLeavesCommunity() {
		given(communityUserRepository.findByUserIdAndCommunityId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "456")).willReturn(
				Optional.of(CommunityUser.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.build())
						.user(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.id(123L)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.role(CommunityRole.MEMBER)
						.build())
		);
		assertDoesNotThrow(() -> communityUserCommandService.remove("456", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@DisplayName("Exception is thrown when there is no deleted community user relationship.")
	@Test
	void exceptionIsThrownWhenThereIsNoCommunityUser() {
		given(communityUserRepository.findByUserIdAndCommunityId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "456")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> communityUserCommandService.remove("456", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@DisplayName("Exception is thrown when if user id is null when removing community user relationship.")
	@Test
	void exceptionIsThrownIfUserIdIsNullWhenRemovingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.remove("456", null));
	}

	@DisplayName("Exception is thrown when if community id is null when removing community user relationship.")
	@Test
	void exceptionIsThrownIfCommunityIdIsNullWhenRemovingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.remove(null, "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@DisplayName("Exception is thrown if community is null when creating community user relationship.")
	@Test
	void exceptionIsThrownIfCommunityIsNullWhenCreatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.create(null,
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				CommunityRole.MEMBER));
	}

	@DisplayName("Exception is thrown if user is null when creating community user relationship.")
	@Test
	void exceptionIsThrownIfUserIsNullWhenCreatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.create(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build(),
				null,
				CommunityRole.MEMBER));
	}

	@DisplayName("Exception is thrown if role is null when creating community user relationship.")
	@Test
	void exceptionIsThrownIfRoleIsNullWhenCreatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.create(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build(),
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				null));
	}

	@DisplayName("Exception is thrown if community is null when updating community user relationship.")
	@Test
	void exceptionIsThrownIfCommunityIsNullWhenUpdatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.update(null,
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				CommunityRole.MANAGER));
	}

	@DisplayName("Exception is thrown if user is null when updating community user relationship.")
	@Test
	void exceptionIsThrownIfUserIsNullWhenUpdatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.update(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build(),
				null,
				CommunityRole.MANAGER));
	}

	@DisplayName("Exception is thrown if role is null when updating community user relationship.")
	@Test
	void exceptionIsThrownIfRoleIsNullWhenUpdatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.update(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build(),
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				null));
	}

	@DisplayName("Exception is thrown if community id is null when updating community user relationship.")
	@Test
	void exceptionIsThrownIfCommunityIdIsNullWhenUpdatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.update(null, "1f37fb2a-b4d0-4119-9113-4677beb20ae2", CommunityRole.MANAGER));
	}

	@DisplayName("Exception is thrown if user id is null when updating community user relationship.")
	@Test
	void exceptionIsThrownIfUserIdIsNullWhenUpdatingCommunityUserRelationship() {
		assertThrows(IllegalArgumentException.class, () -> communityUserCommandService.update("123", null, CommunityRole.MANAGER));
	}

	@DisplayName("Community user relationship is updated.")
	@Test
	void communityUserIsUpdated() {
		given(communityUserRepository.findByUserIdAndCommunityId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123" ))
				.willReturn(Optional.of(CommunityUser.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.build())
						.user(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.id(123L)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.role(CommunityRole.MEMBER)
						.build()));
		given(communityUserRepository.save(argThat(AllOf.allOf(
				isA(CommunityUser.class),
				hasProperty("community", equalTo(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.build())),
				hasProperty("user", equalTo(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build())),
				hasProperty("id", is(123L)),
				hasProperty("creationDate", is(LocalDateTime.of(2019, 1, 15, 20, 0))),
				hasProperty("lastModifiedDate", isA(LocalDateTime.class)),
				hasProperty("role", is(CommunityRole.MANAGER))
		)))).willReturn(
				CommunityUser.builder()
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.build())
						.user(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.id(123L)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.role(CommunityRole.MANAGER)
						.build()
		);
		CommunityUser communityUser = communityUserCommandService.update("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2",CommunityRole.MANAGER);
		assertThat(communityUser).isNotNull();
		assertThat(communityUser.getId()).isEqualTo(123L);
		assertThat(communityUser.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.build());
		assertThat(communityUser.getUser()).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		assertThat(communityUser.getRole()).isEqualTo(CommunityRole.MANAGER);
		assertThat(communityUser.getCreationDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(communityUser.getLastModifiedDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
	}

}

