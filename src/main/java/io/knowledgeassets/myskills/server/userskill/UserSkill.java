package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Data
@RelationshipEntity(type = "RELATED_TO")
public class UserSkill {
	@Id
	@Property(name = "id")
	private String id;
	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	@StartNode
	private User user;
	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	@EndNode
	private Skill skill;
	@Property(name = "currentLevel")
	private Integer currentLevel;
	@Property(name = "desiredLevel")
	private Integer desiredLevel;
	@Property(name = "priority")
	private Integer priority;

	public UserSkill id(String id) {
		this.id = id;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
		generateId();
	}

	public UserSkill user(User user) {
		this.user = user;
		generateId();
		return this;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
		generateId();
	}

	public UserSkill skill(Skill skill) {
		this.skill = skill;
		generateId();
		return this;
	}

	public UserSkill currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public UserSkill desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public UserSkill priority(Integer priority) {
		this.priority = priority;
		return this;
	}

	private void generateId() {
		id = (user != null ? user.getId() : "null") + ';' + (skill != null ? skill.getId() : "null");
	}
}
