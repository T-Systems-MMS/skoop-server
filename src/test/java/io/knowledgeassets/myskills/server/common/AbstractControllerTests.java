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

/**
 * Base class for all Spring {@link org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest} classes.
 * <p>Imports additional context configurations for the following purposes:</p>
 * <ul>
 * <li>Workaround missing Neo4j SessionFactory issue.</li>
 * <li>Enable Spring method security.</li>
 * </ul>
 * <p>Provides convenient builder methods to configure user permissions checked by method security.</p>
 */
@Import({Neo4jSessionFactoryConfiguration.class, MethodSecurityConfiguration.class})
public abstract class AbstractControllerTests {
	@MockBean
	private JwtDecoder jwtDecoder;
	@MockBean
	private UserPermissionQueryService userPermissionQueryService;

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
