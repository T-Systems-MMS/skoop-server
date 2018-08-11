package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.UUID;

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
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User id(String id) {
		this.id = id;
		return this;
	}

	public User newId() {
		id = UUID.randomUUID().toString();
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User userName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public User firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User email(String email) {
		this.email = email;
		return this;
	}

	public List<UserSkill> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
	}

	public User userSkills(List<UserSkill> userSkills) {
		this.userSkills = userSkills;
		return this;
	}
}
