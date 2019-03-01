package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.usernotification.UserNotification;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationStatus;
import io.knowledgeassets.myskills.server.usernotification.UserNotificationType;
import io.knowledgeassets.myskills.server.usernotification.command.UserNotificationCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
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

	@Mock
	private UserNotificationCommandService userNotificationCommandService;

	private CommunityUserCommandService communityUserCommandService;

	@BeforeEach
	void setUp() {
		this.communityUserCommandService = new CommunityUserCommandService(communityRepository, userQueryService, userNotificationCommandService);
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
						.members(new ArrayList<>(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build())))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.type(CommunityType.OPENED)
						.build()
		));

		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("id", is("123")),
						hasProperty("title", is("Java User Group")),
						hasProperty("managers", hasItems(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build())),
						hasProperty("members", hasItems(User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build(),
								User.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.userName("owner")
										.build()
						))
				))
		)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(Arrays.asList(
								User.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.userName("owner")
										.build(),
								User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build()
						))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
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
	@DisplayName("Tests if authenticated user sends a request to join the closed community.")
	void testIfAuthenticatedUserSendsRequestToJoinClosedCommunity() {
		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(Optional.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()));
		given(communityRepository.findById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.type(CommunityType.CLOSED)
						.build()
		));

		given(userNotificationCommandService.sendUserRequestToJoinCommunity(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build(),
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.type(CommunityType.CLOSED)
						.build()
		)).willReturn(UserNotification.builder()
				.community(Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.type(CommunityType.CLOSED)
						.build())
				.initiator(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build())
				.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.status(UserNotificationStatus.PENDING)
				.type(UserNotificationType.COMMUNITY_JOIN_REQUEST)
				.build()
		);

		final Community community = communityUserCommandService.joinCommunityAsMember("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getMembers()).containsExactly(User.builder()
				.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
				.userName("owner")
				.build());
		assertThat(community.getManagers()).containsExactly(User.builder()
				.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
				.userName("owner")
				.build());
		assertThat(community.getType()).isEqualTo(CommunityType.CLOSED);
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
						.members(new ArrayList<>(Arrays.asList(
								User.builder()
										.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
										.userName("tester")
										.build(),
								User.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.userName("owner")
										.build()
						)))
						.managers(singletonList(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.type(CommunityType.OPENED)
						.build()
		));

		given(communityRepository.save(
				argThat(allOf(
						isA(Community.class),
						hasProperty("id", is("123")),
						hasProperty("title", is("Java User Group")),
						hasProperty("members", hasItems(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()
						)),
						hasProperty("managers", hasItems(User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build())
						)
				))
		)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.members(singletonList(
								User.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.userName("owner")
										.build()
						))
						.managers(singletonList(
								User.builder()
								.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
								.userName("owner")
								.build()))
						.build()
		);

		final Community community = communityUserCommandService.leaveCommunity("123", "1f37fb2a-b4d0-4119-9113-4677beb20ae2");
		assertThat(community).isNotNull();
		assertThat(community.getId()).isEqualTo("123");
		assertThat(community.getTitle()).isEqualTo("Java User Group");
		assertThat(community.getMembers()).hasSize(1);
		assertThat(community.getMembers()).contains(User.builder()
				.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
				.userName("owner")
				.build());
		assertThat(community.getManagers()).hasSize(1);
		assertThat(community.getManagers()).contains(User.builder()
				.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
				.userName("owner")
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
						.managers(singletonList(
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
