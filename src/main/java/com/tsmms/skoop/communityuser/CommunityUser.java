package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type = "COMMUNITY_USER")
public class CommunityUser {

	@Id
	@GeneratedValue
	private Long id;

	@Property(name = "role")
	private CommunityRole role;

	@Property(name = "creationDate")
	private LocalDateTime creationDate;

	@Property(name = "lastModifiedDate")
	private LocalDateTime lastModifiedDate;

	@EndNode
	private Community community;

	@StartNode
	private User user;

}
