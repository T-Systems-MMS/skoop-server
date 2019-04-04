package com.tsmms.skoop.userproject.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

}
