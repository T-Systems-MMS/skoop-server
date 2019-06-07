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
@NodeEntity
public class UserPermission extends Permission {

	@Property(name = "scope")
	private UserPermissionScope scope;

	@Relationship(type = "AUTHORIZES")
	private List<User> authorizedUsers;

	@Builder
	public UserPermission(String id, User owner, UserPermissionScope scope, List<User> authorizedUsers) {
		super(id, owner);
		this.scope = scope;
		this.authorizedUsers = authorizedUsers;
	}
}
