package io.knowledgeassets.myskills.server.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

}
