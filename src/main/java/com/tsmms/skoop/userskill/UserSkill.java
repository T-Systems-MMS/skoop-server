package com.tsmms.skoop.userskill;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

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
