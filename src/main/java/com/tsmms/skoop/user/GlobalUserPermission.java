package com.tsmms.skoop.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@Data
public class GlobalUserPermission extends Permission {

	@Property(name = "scope")
	private GlobalUserPermissionScope scope;

	@Builder
	public GlobalUserPermission(String id, User owner, GlobalUserPermissionScope scope) {
		super(id, owner);
		this.scope = scope;
	}
}
