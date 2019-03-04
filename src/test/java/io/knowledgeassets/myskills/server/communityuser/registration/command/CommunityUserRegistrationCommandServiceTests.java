package io.knowledgeassets.myskills.server.communityuser.registration.command;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationRepository;
import io.knowledgeassets.myskills.server.communityuser.registration.command.CommunityUserRegistrationCommandService;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.user.User;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class CommunityUserRegistrationCommandServiceTests {

	@Mock
	private CommunityUserRegistrationRepository communityUserRegistrationRepository;

	@Mock
	private CurrentUserService currentUserService;

	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@BeforeEach
	void setUp() {
		this.communityUserRegistrationCommandService = new CommunityUserRegistrationCommandService(communityUserRegistrationRepository,
				currentUserService);
	}

	@DisplayName("Test if users are invited.")
	@Test
	void testIfUsersAreInvited() {

		given(communityUserRegistrationRepository.saveAll(
				argThat(AllOf.allOf(
						instanceOf(List.class),
						Matchers.hasItems(
								allOf(
										isA(CommunityUserRegistration.class),
										hasProperty("community", equalTo(
												Community.builder()
														.id("123")
														.title("Java User Group")
														.type(CommunityType.OPEN)
														.description("Community for Java developers")
														.build()
										)),
										hasProperty("approvedByCommunity", equalTo(true)),
										hasProperty("approvedByUser", equalTo(false)),
										hasProperty("registeredUser", equalTo(
												User.builder()
														.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
														.userName("Toni")
														.build()
										)),
										hasProperty("id", isA(String.class)),
										hasProperty("creationDatetime", isA(LocalDateTime.class))
								),
								allOf(
										isA(CommunityUserRegistration.class),
										hasProperty("community", equalTo(
												Community.builder()
														.id("123")
														.title("Java User Group")
														.type(CommunityType.OPEN)
														.description("Community for Java developers")
														.build()
										)),
										hasProperty("approvedByCommunity", equalTo(true)),
										hasProperty("approvedByUser", equalTo(false)),
										hasProperty("registeredUser", equalTo(
												User.builder()
														.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
														.userName("Tina")
														.build()
										)),
										hasProperty("id", isA(String.class)),
										hasProperty("creationDatetime", isA(LocalDateTime.class))
								)
						)
				))
		)).willReturn(Arrays.asList(
				CommunityUserRegistration.builder()
						.id("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc")
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.description("Community for Java developers")
								.build())
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("Toni")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(false)
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUserRegistration.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.description("Community for Java developers")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(false)
						.registeredUser(User.builder()
								.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
								.userName("Tina")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));

		List<CommunityUserRegistration> communityUserRegistrations = communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Arrays.asList(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("Toni")
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("Tina")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build());

		assertThat(communityUserRegistrations).hasSize(2);
		CommunityUserRegistration communityUserRegistration = communityUserRegistrations.get(0);
		assertThat(communityUserRegistration.getId()).isEqualTo("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc");
		assertThat(communityUserRegistration.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build());

		assertThat(communityUserRegistration.getRegisteredUser()).isEqualTo(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("Toni")
				.build());
		assertThat(communityUserRegistration.getCreationDatetime()).isOfAnyClassIn(LocalDateTime.class);
		assertThat(communityUserRegistration.getApprovedByCommunity()).isTrue();
		assertThat(communityUserRegistration.getApprovedByUser()).isFalse();

		communityUserRegistration = communityUserRegistrations.get(1);
		assertThat(communityUserRegistration.getId()).isEqualTo("56ef4778-a084-4509-9a3e-80b7895cf7b0");
		assertThat(communityUserRegistration.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build());

		assertThat(communityUserRegistration.getRegisteredUser()).isEqualTo(User.builder()
				.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
				.userName("Tina")
				.build());
		assertThat(communityUserRegistration.getCreationDatetime()).isOfAnyClassIn(LocalDateTime.class);
		assertThat(communityUserRegistration.getApprovedByCommunity()).isTrue();
		assertThat(communityUserRegistration.getApprovedByUser()).isFalse();
	}

	@DisplayName("Exception is thrown when there are no users to invite.")
	@Test
	void exceptionIsThrownWhenNoUsersToInvite() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Collections.emptyList(), Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()));
	}

	@DisplayName("Tests if user request to join a community is sent.")
	@Test
	void testIfUserRequestToJoinCommunityIsSent() {

		User tester = User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.build();

		given(currentUserService.getCurrentUser()).willReturn(tester);

		Community community = Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.description("Community for Java developers")
				.build();

		given(communityUserRegistrationRepository.save(argThat(allOf(
				isA(CommunityUserRegistration.class),
				hasProperty("community", equalTo(
						Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build()
				)),
				hasProperty("approvedByUser", equalTo(true)),
				hasProperty("approvedByCommunity", equalTo(false)),
				hasProperty("registeredUser", equalTo(tester)),
				hasProperty("id", isA(String.class)),
				hasProperty("creationDatetime", isA(LocalDateTime.class))
		)))).willReturn(CommunityUserRegistration.builder()
				.id("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc")
				.community(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.description("Community for Java developers")
						.build())
				.approvedByUser(true)
				.approvedByCommunity(false)
				.registeredUser(tester)
				.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
				.build()
		);

		CommunityUserRegistration communityUserRegistration = communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfUser(tester, community);
		assertThat(communityUserRegistration.getId()).isEqualTo("e156c6e5-8bf2-4c7b-98c1-f3d9b63318fc");
		assertThat(communityUserRegistration.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.description("Community for Java developers")
				.build());
		assertThat(communityUserRegistration.getRegisteredUser()).isEqualTo(tester);
		assertThat(communityUserRegistration.getApprovedByUser()).isTrue();
		assertThat(communityUserRegistration.getApprovedByCommunity()).isFalse();
		assertThat(communityUserRegistration.getCreationDatetime()).isOfAnyClassIn(LocalDateTime.class);
	}

	@DisplayName("Tests if an exception is thrown when user is null when sending a request to join a community.")
	@Test
	void testIfExceptionIsThrownWhenUserIsNullWhenSendingRequestToJoinCommunity() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfUser(null, Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.description("Community for Java developers")
				.build()));
	}

	@DisplayName("Tests if an exception is thrown when community is null when sending a request to join a community.")
	@Test
	void testIfExceptionIsThrownWhenCommunityIsNullWhenSendingRequestToJoinCommunity() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfUser(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.build(), null));
	}

}
