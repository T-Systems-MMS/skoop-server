package io.knowledgeassets.myskills.server.exception.enums;

/**
 * Each model points an Entity. We usually use it in exception messages.
 *
 * @author hadi
 */
public enum Model {

	USER("user"),
	SKILL("skill"),
	USERSKILL("user skill");

	public final String value;

	Model(final String value) {
		this.value = value;
	}

	public String toValue() {
		return this.value;
	}
}
