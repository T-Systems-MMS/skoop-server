package io.knowledgeassets.myskills.server.userskill;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "RELATED_TO")
public class UserSkill {
	@Id
	@GeneratedValue
	private Long id;

	@Property(name = "currentLevel")
	private Integer currentLevel;

	@Property(name = "desiredLevel")
	private Integer desiredLevel;

	@Property(name = "priority")
	private Integer priority;

	@StartNode
	private User user;

	@EndNode
	private Skill skill;
}
