package io.knowledgeassets.myskills.server.skill;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
	private String id;
	private String name;
	private String description;
}
