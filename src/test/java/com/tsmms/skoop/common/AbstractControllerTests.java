package com.tsmms.skoop.common;

import com.tsmms.skoop.security.CurrentUserService;
import com.tsmms.skoop.security.MethodSecurityConfiguration;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.user.query.UserGlobalPermissionQueryService;
import com.tsmms.skoop.user.query.UserPermissionQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;

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
@Import({Neo4jSessionFactoryConfiguration.class, MethodSecurityConfiguration.class, CurrentUserService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractControllerTests {
	@MockBean
	private JwtDecoder jwtDecoder;

	@MockBean
	protected UserPermissionQueryService userPermissionQueryService;

	@MockBean
	protected UserGlobalPermissionQueryService userGlobalPermissionQueryService;

	@Autowired
	protected CurrentUserService currentUserService;

	@BeforeEach
	void prepareSecurityService() {
		willReturn(false).given(userPermissionQueryService).hasUserPermission(any(), any(), any());
		willReturn(false).given(userGlobalPermissionQueryService).isGlobalPermissionGranted(any(), any());
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
