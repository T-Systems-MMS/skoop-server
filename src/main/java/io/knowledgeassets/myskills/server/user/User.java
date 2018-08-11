package io.knowledgeassets.myskills.server.user;

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
public class User {
	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "userName")
	private String userName;
	@Property(name = "firstName")
	private String firstName;
	@Property(name = "lastName")
	private String lastName;
	@Property(name = "email")
	private String email;
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;

	public User id(String id) {
		this.id = id;
		return this;
	}

	public User newId() {
		id = UUID.randomUUID().toString();
		return this;
	}

	public User userName(String userName) {
		this.userName = userName;
		return this;
	}

	public User firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public User lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public User email(String email) {
		this.email = email;
		return this;
	}

	public User userSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
		return this;
	}
}
