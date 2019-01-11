package io.knowledgeassets.myskills.server.userproject;

import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "USER_PROJECT")
public class UserProject {

	@Id
	@GeneratedValue
	private Long id;

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

	@StartNode
	private User user;

	@EndNode
	private Project project;

}
