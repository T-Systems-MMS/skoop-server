package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.UUID;

@Data
@NodeEntity
public class Skill {
	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;

	public Skill id(String id) {
		this.id = id;
		return this;
	}

	public Skill newId() {
		id = UUID.randomUUID().toString();
		return this;
	}

	public Skill name(String name) {
		this.name = name;
		return this;
	}

	public Skill description(String description) {
		this.description = description;
		return this;
	}

	public Skill userSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
		return this;
	}
}
