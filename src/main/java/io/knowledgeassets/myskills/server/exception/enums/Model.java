package io.knowledgeassets.myskills.server.exception.enums;

import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;

/**
 * Each model points an Entity. We usually use it in exception messages.
 *
 * @author hadi
 */
public enum Model {

	USER("user"),
	SKILL("skill"),
	USERSKILL("user skill"),

	UserSkillPriorityReport("user skill priority report"),
	UserSkillPriorityAggregationReport("user skill priority aggregation report"),
	UserSkillReport("user skill report"),
	;

	public final String value;

	Model(final String value) {
		this.value = value;
	}

	public String toValue() {
		return this.value;
	}
}
