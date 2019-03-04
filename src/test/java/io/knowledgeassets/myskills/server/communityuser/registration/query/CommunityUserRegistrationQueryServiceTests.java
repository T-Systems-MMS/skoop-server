package io.knowledgeassets.myskills.server.communityuser.registration.query;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationRepository;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
		final Optional<CommunityUserRegistration> communityUserRegistrationOptional = this.communityUserRegistrationQueryService.getCommunityUserRegistrationById("123");
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

}
