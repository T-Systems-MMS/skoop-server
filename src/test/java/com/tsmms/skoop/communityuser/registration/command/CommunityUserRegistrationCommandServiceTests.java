package com.tsmms.skoop.communityuser.registration.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.command.CommunityUserCommandService;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import com.tsmms.skoop.notification.command.NotificationCommandService;
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

	@Mock
	private CommunityUserCommandService communityUserCommandService;

	@Mock
	private NotificationCommandService notificationCommandService;

	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@BeforeEach
	void setUp() {
		this.communityUserRegistrationCommandService = new CommunityUserRegistrationCommandService(communityUserRegistrationRepository,
				communityUserCommandService, notificationCommandService);
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
										hasProperty("approvedByUser", equalTo(null)),
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
										hasProperty("approvedByUser", equalTo(null)),
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
						.approvedByUser(null)
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
						.approvedByUser(null)
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
		assertThat(communityUserRegistration.getApprovedByUser()).isNull();

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
		assertThat(communityUserRegistration.getApprovedByUser()).isNull();
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

		// given(currentUserService.getCurrentUser()).willReturn(tester);

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
				hasProperty("approvedByCommunity", equalTo(null)),
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
				.approvedByCommunity(null)
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
		assertThat(communityUserRegistration.getApprovedByCommunity()).isNull();
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

	@DisplayName("Approves community user registration by user.")
	@Test
	void approveByUser() {
		given(communityUserRegistrationRepository.save(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build()
		)).willReturn(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build()
		);
		final CommunityUserRegistration registration = communityUserRegistrationCommandService.approve(CommunityUserRegistration.builder()
				.approvedByCommunity(true)
				.approvedByUser(null)
				.registeredUser(User.builder()
						.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
						.userName("tester")
						.build())
				.community(Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.CLOSED)
						.description("Community for Java developers")
						.build())
				.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
				.id("123")
				.build(),
				CommunityUserRegistrationApprovalCommand.builder()
				.approvedByUser(true)
				.approvedByCommunity(null)
				.build()
		);
		assertThat(registration.getId()).isEqualTo("123");
		assertThat(registration.getApprovedByUser()).isTrue();
		assertThat(registration.getApprovedByCommunity()).isTrue();
		assertThat(registration.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(registration.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.description("Community for Java developers")
				.build());
		assertThat(registration.getRegisteredUser()).isEqualTo(
				User.builder()
						.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
						.userName("tester")
						.build()
		);
	}

	@DisplayName("Approves community user registration by community.")
	@Test
	void approveByCommunity() {
		given(communityUserRegistrationRepository.save(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build()
		)).willReturn(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build()
		);
		final CommunityUserRegistration registration = communityUserRegistrationCommandService.approve(CommunityUserRegistration.builder()
						.approvedByCommunity(null)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
						.approvedByUser(null)
						.approvedByCommunity(true)
						.build()
		);
		assertThat(registration.getId()).isEqualTo("123");
		assertThat(registration.getApprovedByUser()).isTrue();
		assertThat(registration.getApprovedByCommunity()).isTrue();
		assertThat(registration.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(registration.getCommunity()).isEqualTo(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.description("Community for Java developers")
				.build());
		assertThat(registration.getRegisteredUser()).isEqualTo(
				User.builder()
						.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
						.userName("tester")
						.build()
		);
	}

	@DisplayName("Exception is thrown when trying to approve as both community and user. No one can approve as both parties at the same time.")
	@Test
	void exceptionIsThrownWhenTryingToApproveAsBothParties() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationCommandService.approve(
				CommunityUserRegistration.builder()
						.approvedByCommunity(null)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
						.approvedByUser(true)
						.approvedByCommunity(true)
						.build()
		));
	}

	@DisplayName("Exception is thrown when community user registration approval command has both flags set to null.")
	@Test
	void exceptionIsThrownWhenCommunityUserRegistrationApprovalCommandHasBothFlagsSetToNull() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationCommandService.approve(
				CommunityUserRegistration.builder()
						.approvedByCommunity(null)
						.approvedByUser(true)
						.registeredUser(User.builder()
								.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
								.userName("tester")
								.build())
						.community(Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.CLOSED)
								.description("Community for Java developers")
								.build())
						.creationDatetime(LocalDateTime.of(2019, 1, 15, 20, 0))
						.id("123")
						.build(),
				CommunityUserRegistrationApprovalCommand.builder()
						.approvedByUser(null)
						.approvedByCommunity(null)
						.build()
		));
	}

}
