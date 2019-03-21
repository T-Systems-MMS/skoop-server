package io.knowledgeassets.myskills.server.exception;

/**
 * Thrown to indicate there is an issue in user community access / interaction.
 */
public class UserCommunityException extends BusinessException {

	public UserCommunityException() {
	}

	public UserCommunityException(String message) {
		super(message);
	}
}
