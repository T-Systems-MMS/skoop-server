package io.knowledgeassets.myskills.server.security;

import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import org.apache.commons.lang3.StringUtils;
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

	public boolean hasCommunityManagerRole(Jwt jwt, String communityId) {
		final String userIdClaim = jwt.getClaimAsString(MYSKILLS_USER_ID);
		if (StringUtils.isEmpty(userIdClaim)) {
			throw new IllegalArgumentException("User identifier is not defined.");
		} else {
			return communityQueryService.hasCommunityManagerRole(userIdClaim, communityId);
		}
	}

}
