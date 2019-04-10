package com.tsmms.skoop.communityuser.registration.query;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CommunityUserRegistrationQueryServiceTests {

	@Mock
	private CommunityUserRegistrationRepository communityUserRegistrationRepository;

	private CommunityUserRegistrationQueryService communityUserRegistrationQueryService;

	@BeforeEach
	void setUp() {
		this.communityUserRegistrationQueryService = new CommunityUserRegistrationQueryService(communityUserRegistrationRepository);
	}

	@DisplayName("Get user registration by ID.")
	@Test
	void getUserRegistrationById() {
		given(communityUserRegistrationRepository.findById("123")).willReturn(Optional.of(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(false)
						.registeredUser(User.builder()
								.id("123")
								.userName("tester")
								.build()
						)
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 20, 0))
						.community(Community.builder()
								.title("Java User Group")
								.id("456")
								.type(CommunityType.OPEN)
								.build()
						)
						.build()
				)
		);
		final Optional<CommunityUserRegistration> communityUserRegistrationOptional = communityUserRegistrationQueryService.getCommunityUserRegistrationById("123");
		assertThat(communityUserRegistrationOptional).isNotEmpty();
		final CommunityUserRegistration communityUserRegistration = communityUserRegistrationOptional.get();
		assertThat(communityUserRegistration.getApprovedByCommunity()).isTrue();
		assertThat(communityUserRegistration.getApprovedByUser()).isFalse();
		assertThat(communityUserRegistration.getRegisteredUser()).isEqualTo(User.builder()
				.id("123")
				.userName("tester")
				.build());
		assertThat(communityUserRegistration.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 20, 20, 0));
		assertThat(communityUserRegistration.getCommunity()).isEqualTo(Community.builder()
				.title("Java User Group")
				.id("456")
				.type(CommunityType.OPEN)
				.build());
	}

	@DisplayName("Get pending request to join a community.")
	@Test
	void getPendingUserRequestToJoinCommunity() {
		given(communityUserRegistrationRepository.findByRegisteredUserIdAndCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull("123", "456")).willReturn(
				Optional.of(
						CommunityUserRegistration.builder()
								.approvedByCommunity(false)
								.approvedByUser(true)
								.community(Community.builder()
										.title("Java User Group")
										.id("456")
										.type(CommunityType.OPEN)
										.build())
								.registeredUser(User.builder()
										.id("123")
										.userName("tester")
										.build())
								.id("abc")
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 10, 0))
								.build()
				)
		);
		Optional<CommunityUserRegistration> userRegistrationOptional = communityUserRegistrationQueryService.getPendingUserRequestToJoinCommunity("123", "456");
		assertThat(userRegistrationOptional).isNotEmpty();
		CommunityUserRegistration registration = userRegistrationOptional.get();
		assertThat(registration.getId()).isEqualTo("abc");
		assertThat(registration.getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 1, 20, 10, 0));
		assertThat(registration.getRegisteredUser()).isEqualTo(User.builder()
				.id("123")
				.userName("tester")
				.build());
		assertThat(registration.getCommunity()).isEqualTo(Community.builder()
				.title("Java User Group")
				.id("456")
				.type(CommunityType.OPEN)
				.build());
		assertThat(registration.getApprovedByCommunity()).isFalse();
		assertThat(registration.getApprovedByUser()).isTrue();
	}

	@DisplayName("Gets pending invitations to join community.")
	@Test
	void getsPendingInvitationsToJoinCommunity() {
		given(communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsNullAndApprovedByCommunityIsTrue("123")).willReturn(
			Stream.of(
					CommunityUserRegistration.builder()
							.approvedByCommunity(true)
							.approvedByUser(null)
							.community(Community.builder()
									.title("Java User Group")
									.id("123")
									.type(CommunityType.OPEN)
									.build())
							.registeredUser(User.builder()
									.id("456")
									.userName("tester")
									.build())
							.id("abc")
							.creationDatetime(LocalDateTime.of(2019, 1, 20, 10, 0))
							.build()
			)
		);
		final Stream<CommunityUserRegistration> communityUserRegistrationStream = communityUserRegistrationQueryService.getPendingInvitationsToJoinCommunity("123");
		assertThat(communityUserRegistrationStream).containsExactlyInAnyOrder(
				CommunityUserRegistration.builder()
						.approvedByCommunity(true)
						.approvedByUser(null)
						.community(Community.builder()
								.title("Java User Group")
								.id("123")
								.type(CommunityType.OPEN)
								.build())
						.registeredUser(User.builder()
								.id("456")
								.userName("tester")
								.build())
						.id("abc")
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 10, 0))
						.build()
		);
	}

	@DisplayName("Throws exception if null is passed as community id when getting pending invitations to join community.")
	@Test
	void throwsExceptionIfNullIsPassedAsCommunityIdWhenGettingPendingInvitationsToJoinCommunity() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationQueryService.getPendingInvitationsToJoinCommunity(null));
	}

	@DisplayName("Gets pending user requests to join community.")
	@Test
	void getsPendingUserRequestsToJoinCommunity() {
		given(communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull("123")).willReturn(
				Stream.of(
						CommunityUserRegistration.builder()
								.approvedByCommunity(null)
								.approvedByUser(true)
								.community(Community.builder()
										.title("Java User Group")
										.id("123")
										.type(CommunityType.OPEN)
										.build())
								.registeredUser(User.builder()
										.id("456")
										.userName("tester")
										.build())
								.id("abc")
								.creationDatetime(LocalDateTime.of(2019, 1, 20, 10, 0))
								.build()
				)
		);
		final Stream<CommunityUserRegistration> communityUserRegistrationStream = communityUserRegistrationQueryService.getPendingUserRequestsToJoinCommunity("123");
		assertThat(communityUserRegistrationStream).containsExactlyInAnyOrder(
				CommunityUserRegistration.builder()
						.approvedByCommunity(null)
						.approvedByUser(true)
						.community(Community.builder()
								.title("Java User Group")
								.id("123")
								.type(CommunityType.OPEN)
								.build())
						.registeredUser(User.builder()
								.id("456")
								.userName("tester")
								.build())
						.id("abc")
						.creationDatetime(LocalDateTime.of(2019, 1, 20, 10, 0))
						.build()
		);
	}


	@DisplayName("Throws exception if null is passed as community id when getting pending user requests to join community.")
	@Test
	void throwsExceptionIfNullIsPassedAsCommunityIdWhenGettingPendingUserRequestsToJoinCommunity() {
		assertThrows(IllegalArgumentException.class, () -> communityUserRegistrationQueryService.getPendingUserRequestsToJoinCommunity(null));
	}

}
