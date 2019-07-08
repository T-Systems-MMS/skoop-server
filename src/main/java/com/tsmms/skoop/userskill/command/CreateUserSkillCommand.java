package com.tsmms.skoop.userskill.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserSkillCommand {

	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;
	private Boolean favourite;

}
