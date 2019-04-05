package com.tsmms.skoop.security;

import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
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

//	@Mock
//	private CommunityQueryService communityQueryService;
//
//	@Mock
//	private CurrentUserService currentUserService;
//
//	@Mock
//	private UserPermissionQueryService userPermissionQueryService;
//
//	private SecurityService securityService;
//
//	@BeforeEach
//	void setUp() {
//		this.securityService = new SecurityService(communityQueryService, currentUserService, userPermissionQueryService);
//	}
//
//	@DisplayName("The user has community manager role.")
//	@Test
//	void userHasCommunityManagerRole() {
//		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(true);
//		assertThat(securityService.isCommunityManager("123", "456")).isTrue();
//	}
//
//	@DisplayName("The user does not have community manager role.")
//	@Test
//	void userDoesNotHaveCommunityManagerRole() {
//		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(false);
//		assertThat(securityService.isCommunityManager("123", "456")).isFalse();
//	}
//
//	@DisplayName("The authenticated user has community manager role.")
//	@Test
//	void authenticatedUserHasCommunityManagerRole() {
//		given(currentUserService.getCurrentUser()).willReturn(User.builder()
//				.id("123")
//				.build());
//		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(true);
//		assertThat(securityService.isCommunityManager( "456")).isTrue();
//	}
//
//	@DisplayName("The authenticated user does not have community manager role.")
//	@Test
//	void authenticatedUserDoesNotHaveCommunityManagerRole() {
//		given(currentUserService.getCurrentUser()).willReturn(User.builder()
//				.id("123")
//				.build());
//		given(communityQueryService.hasCommunityManagerRole("123", "456")).willReturn(false);
//		assertThat(securityService.isCommunityManager("456")).isFalse();
//	}
//
//	@DisplayName("Throws an exception when user identifier is not defined when checking on having community manager role.")
//	@Test
//	void throwsExceptionWhenUserIdentifierIsNotDefined() {
//		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityManager("", "456"));
//	}
//
//	@DisplayName("Throws an exception when community identifier is not defined when checking on having community manager role.")
//	@Test
//	void throwsExceptionWhenCommunityIdentifierIsNotDefined() {
//		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityManager("123", ""));
//	}
//
//	@DisplayName("The user has community member role.")
//	@Test
//	void userHasCommunityMemberRole() {
//		given(communityQueryService.isCommunityMember("123", "456")).willReturn(true);
//		assertThat(securityService.isCommunityMember("123", "456")).isTrue();
//	}
//
//	@DisplayName("The user does not have community member role.")
//	@Test
//	void userDoesNotHaveCommunityMemberRole() {
//		given(communityQueryService.isCommunityMember("123", "456")).willReturn(false);
//		assertThat(securityService.isCommunityMember("123", "456")).isFalse();
//	}
//
//	@DisplayName("The authenticated user has community member role.")
//	@Test
//	void authenticatedUserHasCommunityMemberRole() {
//		given(currentUserService.getCurrentUser()).willReturn(User.builder()
//				.id("123")
//				.build());
//		given(communityQueryService.isCommunityMember("123", "456")).willReturn(true);
//		assertThat(securityService.isCommunityMember( "456")).isTrue();
//	}
//
//	@DisplayName("The authenticated user does not have community member role.")
//	@Test
//	void authenticatedUserDoesNotHaveCommunityMemberRole() {
//		given(currentUserService.getCurrentUser()).willReturn(User.builder()
//				.id("123")
//				.build());
//		given(communityQueryService.isCommunityMember("123", "456")).willReturn(false);
//		assertThat(securityService.isCommunityMember("456")).isFalse();
//	}
//
//	@DisplayName("Throws an exception when user identifier is not defined when checking community membership.")
//	@Test
//	void throwsExceptionWhenUserIdentifierIsNotDefinedWhenCheckingCommunityMembership() {
//		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityMember("", "456"));
//	}
//
//	@DisplayName("Throws an exception when community identifier is not defined when checking community membership.")
//	@Test
//	void throwsExceptionWhenCommunityIdentifierIsNotDefinedWhenCheckingCommunityMembership() {
//		assertThrows(IllegalArgumentException.class, () -> securityService.isCommunityMember("123", ""));
//	}
//
//	@DisplayName("Authenticated user ID is the ID of the authenticated user.")
//	@Test
//	void isAuthenticatedUserIdEqualToIdOfAuthenticatedUser() {
//		given(currentUserService.getCurrentUser()).willReturn(User.builder()
//				.id("123")
//				.build());
//		assertThat(securityService.isAuthenticatedUserId("123")).isTrue();
//	}
//
//	@DisplayName("Authenticated user ID is the expected one.")
//	@Test
//	void isAuthenticatedUserIdEqualToExpectedId() {
//		assertThat(securityService.isAuthenticatedUserId("123", "123")).isTrue();
//	}
//
//	@DisplayName("User has the specified permission.")
//	@Test
//	void userHasPermission() {
//		given(userPermissionQueryService.hasUserPermission("123", "456", UserPermissionScope.READ_USER_SKILLS)).willReturn(true);
//		assertThat(securityService.hasUserPermission("123", "456", UserPermissionScope.READ_USER_SKILLS)).isTrue();
//	}
//
//	@DisplayName("User does not have the specified permission.")
//	@Test
//	void userDoesNotHavePermission() {
//		given(userPermissionQueryService.hasUserPermission("123", "456", UserPermissionScope.READ_USER_SKILLS)).willReturn(false);
//		assertThat(securityService.hasUserPermission("123", "456", UserPermissionScope.READ_USER_SKILLS)).isFalse();
//	}

}
