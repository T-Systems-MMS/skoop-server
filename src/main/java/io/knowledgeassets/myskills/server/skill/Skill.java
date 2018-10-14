package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import javax.validation.constraints.NotBlank;
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

	@NotBlank
	@Size(min = 3, max = 64)
	@Property(name = "name")
	@Index(unique = true)
	private String name;

	@Property(name = "description")
	private String description;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATED_TO", direction = Relationship.UNDIRECTED)
	private List<UserSkill> userSkills;

	@EqualsAndHashCode.Exclude
	private List<SkillGroup> skillGroups;

}
