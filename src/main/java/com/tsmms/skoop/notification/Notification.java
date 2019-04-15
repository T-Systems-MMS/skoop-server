package com.tsmms.skoop.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public abstract class Notification {

	@Id
	@Property(name = "id")
	private String id;

	@Property(name = "creationDatetime")
	private LocalDateTime creationDatetime;

	public boolean isToDo() {
		return false;
	}

}
