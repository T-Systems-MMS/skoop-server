package io.knowledgeassets.myskills.server.common;

import io.knowledgeassets.myskills.server.security.UserIdentity;
import io.knowledgeassets.myskills.server.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

public class UserIdentityAuthenticationFactory {
	public static Authentication withUser(User user, String authorities) {
		UserIdentity principal = UserIdentity.of(user, commaSeparatedStringToAuthorityList(authorities));
		return new UsernamePasswordAuthenticationToken(principal, "N/A", principal.getAuthorities());
	}
}
