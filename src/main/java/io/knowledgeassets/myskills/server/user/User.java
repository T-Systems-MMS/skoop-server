package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.List;

import static org.neo4j.ogm.annotation.Relationship.UNDIRECTED;

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

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = UNDIRECTED)
	private List<UserSkill> userSkills;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "HAS_GRANTED")
	private List<UserPermission> userPermissions;
}
