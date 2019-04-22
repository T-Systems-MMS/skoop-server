package com.tsmms.skoop.membership;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Membership {

	@Id
	@Property(name = "id")
	private String id;
	@Property(name = "name")
	private String name;
	@Property(name = "description")
	private String description;
	@Property(name = "link")
	private String link;
	@Property(name = "creationDatetime")
	private LocalDateTime creationDatetime;
	@Property(name = "lastModifiedDatetime")
	private LocalDateTime lastModifiedDatetime;
	@Relationship(type = "REFERS_TO_SKILL")
	private List<Skill> skills;
	@Relationship(type = "USER")
	private User user;

}
