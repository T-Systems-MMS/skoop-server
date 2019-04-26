package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestimonialUpdateCommand {

	private String author;
	private String comment;
	private Set<Skill> skills;

}
