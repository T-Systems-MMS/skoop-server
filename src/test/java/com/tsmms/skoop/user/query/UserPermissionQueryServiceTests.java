package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermission;
import com.tsmms.skoop.user.UserPermissionRepository;
import com.tsmms.skoop.user.UserPermissionScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserPermissionQueryServiceTests {

	@Mock
	private UserPermissionRepository userPermissionRepository;

	private UserPermissionQueryService userPermissionQueryService;

	@BeforeEach
	void setUp() {
		userPermissionQueryService = new UserPermissionQueryService(userPermissionRepository);
	}

	@Test
	@DisplayName("fetches inbound user permissions which were granted to the authorized user.")
	void fetchesInboundUserPermissionsByAuthorizedUserId() {

		User owner = User.builder().id("123").userName("owner")
				.firstName("owner").email("owner@gmail.com").build();

		User authorizedUser = User.builder().id("456").userName("authorizedUser")
				.firstName("authorizedUser").email("authorizeduser@gmail.com").build();

		UserPermission userPermission = UserPermission.builder()
				.owner(owner)
				.authorizedUsers(Collections.singletonList(authorizedUser))
				.scope(UserPermissionScope.READ_USER_SKILLS)
				.id("ABC")
				.build();

		given(userPermissionRepository.findByAuthorizedUsersId("123")).willReturn(Collections.singletonList(userPermission));

		Stream<UserPermission> userPermissions = userPermissionQueryService.getInboundUserPermissionsByAuthorizedUserId("123");

		assertThat(userPermissions).isNotNull();
		List<UserPermission> userPermissionsList = userPermissions.collect(toList());
		assertThat(userPermissionsList).hasSize(1);
		UserPermission result = userPermissionsList.get(0);
		assertThat(result.getId()).isEqualTo("ABC");
		assertThat(result.getScope()).isEqualTo(UserPermissionScope.READ_USER_SKILLS);
		assertThat(result.getOwner()).isEqualTo(owner);
		assertThat(result.getAuthorizedUsers()).isEqualTo(Collections.singletonList(authorizedUser));
	}

}
