package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static io.knowledgeassets.myskills.server.security.JwtClaims.MYSKILLS_USER_ID;
import static java.util.Objects.requireNonNull;

/**
 * Service to accumulate security logic used by {@link MySkillsSecurityExpressionRoot}
 * to make it available withing the whole application.
 */
@Service
public class SecurityService {

	private final CommunityQueryService communityQueryService;

	public SecurityService(CommunityQueryService communityQueryService) {
		this.communityQueryService = requireNonNull(communityQueryService);
	}

	public boolean hasCommunityManagerRole(Object principal, String communityId) {
		if (principal instanceof Jwt) {
			final String userIdClaim = ((Jwt) principal).getClaimAsString(MYSKILLS_USER_ID);
			return communityQueryService.hasCommunityManagerRole(userIdClaim, communityId);
		}
		return false;
	}

}
