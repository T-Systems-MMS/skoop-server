package com.tsmms.skoop.userproject;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserProject {

	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "role")
	private String role;

	@Property(name = "tasks")
	private String tasks;

	@NotNull
	@Property(name = "startDate")
	private LocalDate startDate;

	@Property(name = "endDate")
	private LocalDate endDate;

	@NotNull
	@Property(name = "creationDate")
	private LocalDateTime creationDate;

	@NotNull
	@Property(name = "lastModifiedDate")
	private LocalDateTime lastModifiedDate;

	@Relationship(type = "REFERS_TO_SKILL")
	private Set<Skill> skills;

	@Relationship(type = "USER")
	private User user;

	@Relationship(type = "PROJECT")
	private Project project;

}
