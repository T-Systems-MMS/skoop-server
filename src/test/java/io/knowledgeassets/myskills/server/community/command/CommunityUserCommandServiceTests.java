package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.exception.CommunityAccessDeniedException;
import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class CommunityUserCommandServiceTests {

	@Mock
	private CommunityRepository communityRepository;

	@Mock
	private UserQueryService userQueryService;

	private CommunityUserCommandService communityUserCommandService;

	@BeforeEach
	void setUp() {
		this.communityUserCommandService = new CommunityUserCommandService(communityRepository, userQueryService);
	}

	@Test
	@DisplayName("Tests if authenticated user joins the community.")
	void testIfAuthenticatedUserJoinsCommunity() {

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()));
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.build()
		));

		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("id", is("123")),
						hasProperty("title", is("Java User Group")),
						hasProperty("members", equalTo(singletonList(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build()
						)))
				))
		)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(
								User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()
						))
						.build()
		);

		final Community community = communityUserCommandService.joinCommunityAsMember("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getMembers()).contains(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
	}

	@Test
	@DisplayName("Tests if exception is thrown when authenticated user joins non-existent community.")
	void testIfExceptionIsThrownWhenAuthenticatedUserJoinsNonExistentCommunity() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(communityRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> communityUserCommandService.joinCommunityAsMember("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@Test
	@DisplayName("Tests if an exception is thrown when joining closed community.")
	void testIfExceptionIsThrownWhenJoiningClosedCommunity() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.build()
		));
		assertThrows(CommunityAccessDeniedException.class, () -> communityUserCommandService.joinCommunityAsMember("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@Test
	@DisplayName("Tests if authenticated user leaves the community.")
	void testIfAuthenticatedUserLeavesCommunity() {

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(Arrays.asList(
								User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build(),
								User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("anotherTester")
										.build()
						))
						.type(CommunityType.OPENED)
						.build()
		));

		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("id", is("123")),
						hasProperty("title", is("Java User Group")),
						hasProperty("members", equalTo(singletonList(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("anotherTester")
								.build()
						)))
				))
		)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(
								User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("anotherTester")
										.build()
						))
						.build()
		);

		final Community community = communityUserCommandService.leaveCommunity("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers()).contains(User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("anotherTester")
				.build());
	}

	@Test
	@DisplayName("Tests if exception is thrown when authenticated user leaves the community he is not a member of.")
	void testIfExceptionIsThrownWhenAuthenticatedUserLeavesCommunityHeIsNotMemberOf() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(
								User.builder()
										.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
										.userName("anotherTester")
										.build()
						))
						.type(CommunityType.OPENED)
						.build()
		));

		assertThrows(InvalidInputException.class, () -> communityUserCommandService.leaveCommunity("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

	@Test
	@DisplayName("Tests if exception is thrown when authenticated user leaves non-existent community.")
	void testIfExceptionIsThrownWhenAuthenticatedUserLeavesNonExistentCommunity() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		));
		given(communityRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> communityUserCommandService.leaveCommunity("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2"));
	}

}
