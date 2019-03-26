package io.knowledgeassets.myskills.server.notification;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {

	INVITATION_TO_JOIN_COMMUNITY("INVITATION_TO_JOIN_COMMUNITY"),
	REQUEST_TO_JOIN_COMMUNITY("REQUEST_TO_JOIN_COMMUNITY"),
	MEMBER_LEFT_COMMUNITY("MEMBER_LEFT_COMMUNITY"),
	COMMUNITY_ROLE_CHANGED("COMMUNITY_ROLE_CHANGED"),
	COMMUNITY_CHANGED("COMMUNITY_CHANGED"),
	MEMBER_KICKED_OUT_OF_COMMUNITY("MEMBER_KICKED_OUT_OF_COMMUNITY"),
	COMMUNITY_DELETED("COMMUNITY_DELETED"),
	ACCEPTANCE_TO_COMMUNITY("ACCEPTANCE_TO_COMMUNITY");

	/**
	 * The field to decouple enum values and their business representation.
	 */
	private final String name;

	NotificationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@JsonValue
	@Override
	public String toString() {
		return name;
	}

}
