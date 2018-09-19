package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class User {

	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "userName")
	@Index(unique = true)
	private String userName;
	@Property(name = "firstName")
	private String firstName;
	@Property(name = "lastName")
	private String lastName;
	@Property(name = "email")
	private String email;
	@Property(name = "coach")
	private Boolean coach;

	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;
}
