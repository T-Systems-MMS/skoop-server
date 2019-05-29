package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProjectCommand {

	private String role;
	private String tasks;
	@NotNull
	private LocalDate startDate;
	private LocalDate endDate;
	private Set<Skill> skills;
	private boolean approved;

}
