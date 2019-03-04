package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTests {

	@Mock
	private CommunityQueryService communityQueryService;

	@Mock
	private CurrentUserService currentUserService;

	@Mock
	private UserPermissionQueryService userPermissionQueryService;

	private SecurityService securityService;

	@BeforeEach
	void setUp() {
		this.securityService = new SecurityService(communityQueryService, currentUserService, userPermissionQueryService);
	}

	@DisplayName("The user has community manager role.")
	@Test
	void userHasCommunityManagerRole() {
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(true);
		assertThat(securityService.isCommunityManager("123", "456")).isTrue();
	}

	@DisplayName("The user does not have community manager role.")
	@Test
	void userDoesNotHaveCommunityManagerRole() {
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(false);
		assertThat(securityService.isCommunityManager("123", "456")).isFalse();
	}

	@DisplayName("The authenticated user has community manager role.")
	@Test
	void authenticatedUserHasCommunityManagerRole() {
		given(currentUserService.getCurrentUser()).willReturn(User.builder()
				.id("123")
				.build());
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(true);
		assertThat(securityService.isCommunityManager( "456")).isTrue();
	}

	@DisplayName("The authenticated user does not have community manager role.")
	@Test
	void authenticatedUserDoesNotHaveCommunityManagerRole() {
		given(currentUserService.getCurrentUser()).willReturn(User.builder()
				.id("123")
				.build());
		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(false);
		assertThat(securityService.isCommunityManager("456")).isFalse();
	}

	@DisplayName("Throws an exception when user identifier is not defined.")
	@Test
	void throwsExceptionWhenUserIdentifierIsNotDefined() {
		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityManager("", "456"));
	}

	@DisplayName("Throws an exception when community identifier is not defined.")
	@Test
	void throwsExceptionWhenCommunityIdentifierIsNotDefined() {
		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityManager("123", ""));
	}

}
