package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Community {

	@Id
	@Property(name = "id")
	private String id;
	@NotBlank
	@Property(name = "title")
	private String title;
	@Property(name = "type")
	private CommunityType type;
	@Property(name = "description")
	private String description;
	@Relationship(type = "HAS_LINK", direction = Relationship.UNDIRECTED)
	private List<Link> links;
	@NotNull
	@Property(name = "creationDate")
	private LocalDateTime creationDate;

	@NotNull
	@Property(name = "lastModifiedDate")
	private LocalDateTime lastModifiedDate;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "HAS_MEMBER", direction = Relationship.UNDIRECTED)
	private List<User> members;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "MANAGED_BY", direction = Relationship.UNDIRECTED)
	private List<User> managers;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "RELATES_TO", direction = Relationship.OUTGOING)
	private List<Skill> skills;

}
