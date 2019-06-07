package com.tsmms.skoop.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@Data
@AllArgsConstructor
public abstract class Permission {

	@Id
	@Property(name = "id")
	private String id;

	@Relationship(type = "HAS_GRANTED", direction = INCOMING)
	private User owner;

}
