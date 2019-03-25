package io.knowledgeassets.myskills.server.notification;

public enum NotificationType {

	INVITATION_TO_JOIN_COMMUNITY("INVITATION_TO_JOIN_COMMUNITY"),
	REQUEST_TO_JOIN_COMMUNITY("REQUEST_TO_JOIN_COMMUNITY"),
	MEMBER_LEFT_COMMUNITY("MEMBER_LEFT_COMMUNITY"),
	COMMUNITY_ROLE_CHANGED("COMMUNITY_ROLE_CHANGED"),
	COMMUNITY_CHANGED("COMMUNITY_CHANGED"),
	MEMBER_KICKED_OUT_OF_COMMUNITY("MEMBER_KICKED_OUT_OF_COMMUNITY"),
	COMMUNITY_DELETED("COMMUNITY_DELETED"),
	ACCEPTANCE_TO_COMMUNITY("ACCEPTANCE_TO_COMMUNITY");

	private final String name;

	NotificationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
