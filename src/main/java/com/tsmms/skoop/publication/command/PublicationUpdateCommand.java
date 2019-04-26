package com.tsmms.skoop.publication.command;

import com.tsmms.skoop.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationUpdateCommand {

	private String title;
	private LocalDate date;
	private String publisher;
	private String link;
	private Set<Skill> skills;

}
