package com.tsmms.skoop.skillgroup;

import lombok.*;
import org.neo4j.ogm.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class SkillGroup {

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

}
