package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.userskill.UserSkill;
import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Skill {

	@Id
	@Property(name = "id")
	private String id;

	@NotNull
	@NotBlank
	@Size(max = 64)
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;
}
