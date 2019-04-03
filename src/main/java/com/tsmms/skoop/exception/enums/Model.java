package com.tsmms.skoop.exception.enums;

/**
 * Each model points an Entity. We usually use it in exception messages.
 *
 * @author hadi
 */
public enum Model {

	USER("user"),
	SKILL("skill"),
	SKILL_GROUP("skill group"),
	USER_SKILL("user skill"),
	USER_PROJECT("user project"),
	PROJECT("project"),
	COMMUNITY("community"),
	COMMUNITY_USER("community user"),
	USER_REGISTRATION("user registration"),

	USER_SKILL_PRIORITY_REPORT("user skill priority report"),
	USER_SKILL_PRIORITY_AGGREGATION_REPORT("user skill priority aggregation report"),
	USER_SKILL_REPORT("user skill report"),
	;

	public final String value;

	Model(final String value) {
		this.value = value;
	}

	public String toValue() {
		return this.value;
	}
}
