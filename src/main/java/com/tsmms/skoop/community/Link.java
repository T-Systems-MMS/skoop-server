package com.tsmms.skoop.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Link {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String href;

}
