package io.knowledgeassets.myskills.server.search;

import lombok.Builder;
import lombok.Data;

/**
 * A criterion to search user by.
 */
@Data
@Builder
public class UserSearchSkillCriterion {

	/**
	 * An identifier of a skill.
	 */
	private String skillId;
	/**
	 * Minimum current level of a skill.
	 */
	private Integer minimumCurrentLevel;

}
