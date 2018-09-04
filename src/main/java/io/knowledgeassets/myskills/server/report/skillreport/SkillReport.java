package io.knowledgeassets.myskills.server.report.skillreport;

import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class SkillReport {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;
}
