package com.tsmms.skoop.project;

import com.tsmms.skoop.userproject.UserProject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Project {

	@Id
	@Property(name = "id")
	private String id;

	@NotBlank
	@Property(name = "name")
	@Index(unique = true)
	private String name;

	@Property(name = "customer")
	private String customer;

	@Property(name = "industrySector")
	private String industrySector;

	@Property(name = "description")
	private String description;

	@NotNull
	@Property(name = "creationDate")
	private LocalDateTime creationDate;

	@NotNull
	@Property(name = "lastModifiedDate")
	private LocalDateTime lastModifiedDate;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@Relationship(type = "USER_PROJECT", direction = Relationship.UNDIRECTED)
	private List<UserProject> userProjects;

}
