package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.user.query.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "RELATED_TO")
public class UserSkill {
	@Id
	@Property(name = "id")
	private String id;
	@StartNode
	private User user;
	@EndNode
	private Skill skill;
	@Property(name = "currentLevel")
	private Integer currentLevel;
	@Property(name = "desiredLevel")
	private Integer desiredLevel;
	@Property(name = "priority")
	private Integer priority;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserSkill id(String id) {
		this.id = id;
		return this;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserSkill user(User user) {
		this.user = user;
		return this;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public UserSkill skill(Skill skill) {
		this.skill = skill;
		return this;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	public UserSkill currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public Integer getDesiredLevel() {
		return desiredLevel;
	}

	public void setDesiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
	}

	public UserSkill desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public UserSkill priority(Integer priority) {
		this.priority = priority;
		return this;
	}
}
