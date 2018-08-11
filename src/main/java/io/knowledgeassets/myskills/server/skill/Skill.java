package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.UUID;

@NodeEntity
public class Skill {
	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Skill id(String id) {
		this.id = id;
		return this;
	}

	public Skill newId() {
		id = UUID.randomUUID().toString();
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Skill name(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Skill description(String description) {
		this.description = description;
		return this;
	}

	public List<UserSkill> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
	}

	public Skill userSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
		return this;
	}
}
