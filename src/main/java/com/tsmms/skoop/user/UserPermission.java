package com.tsmms.skoop.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class UserPermission {
	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "scope")
	private UserPermissionScope scope;

	@Relationship(type = "HAS_GRANTED", direction = INCOMING)
	private User owner;

	@Relationship(type = "AUTHORIZES")
	private List<User> authorizedUsers;
}
