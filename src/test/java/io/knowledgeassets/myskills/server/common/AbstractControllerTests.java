package io.knowledgeassets.myskills.server.common;

import io.knowledgeassets.myskills.server.security.MethodSecurityConfiguration;
import io.knowledgeassets.myskills.server.user.UserPermissionScope;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;

// Additional Neo4j configuration is required to workaround missing SessionFactory issue!
@Import({Neo4jSessionFactoryConfiguration.class, MethodSecurityConfiguration.class})
public abstract class AbstractControllerTests {
	@MockBean
	protected JwtDecoder jwtDecoder;
	@MockBean
	protected UserPermissionQueryService userPermissionQueryService;

	@BeforeEach
	void prepareUserPermissions() {
		willReturn(false).given(userPermissionQueryService).hasUserPermission(any(), any(), any());
	}

	protected UserConfigurationBuilder givenUser(String userId) {
		return new UserConfigurationBuilder(userId);
	}

	public class UserConfigurationBuilder {
		private final String userId;

		private UserConfigurationBuilder(String userId) {
			this.userId = userId;
		}

		public UserPermissionBuilder hasAuthorizedUsers(String... userIds) {
			return new UserPermissionBuilder(asList(userIds));
		}

		public class UserPermissionBuilder {
			private final Collection<String> authorizedUserIds;

			private UserPermissionBuilder(Collection<String> authorizedUserIds) {
				this.authorizedUserIds = authorizedUserIds;
			}

			public void forScopes(UserPermissionScope... scopes) {
				for (UserPermissionScope scope : scopes) {
					for (String authorizedUserId : authorizedUserIds) {
						willReturn(true).given(userPermissionQueryService)
								.hasUserPermission(userId, authorizedUserId, scope);
					}
				}
			}
		}
	}
}
