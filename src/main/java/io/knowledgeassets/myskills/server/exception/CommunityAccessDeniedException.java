package io.knowledgeassets.myskills.server.exception;

/**
 * Thrown to indicate an access to community is denied.
 */
public class CommunityAccessDeniedException extends BusinessException {

	public CommunityAccessDeniedException(String message) {
		super(message);
	}
}
