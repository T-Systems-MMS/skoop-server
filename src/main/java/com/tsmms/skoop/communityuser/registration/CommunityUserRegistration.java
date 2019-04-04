package com.tsmms.skoop.communityuser.registration;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.CommunityUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class CommunityUserRegistration {

	@Id
	@Property(name = "id")
	private String id;
	@Relationship(type = "community")
	private Community community;
	@Relationship(type = "registeredUser")
	private User registeredUser;
	@Property(name = "creationDatetime")
	@NotNull
	private LocalDateTime creationDatetime;
	@Property("approvedByUser")
	private Boolean approvedByUser;
	@Property("approvedByCommunity")
	private Boolean approvedByCommunity;
	/**
	 * Community-user relationship created in case registration was approved.
	 */
	@Transient
	private CommunityUser communityUser;

}
