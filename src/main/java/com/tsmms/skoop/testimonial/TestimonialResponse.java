package com.tsmms.skoop.testimonial;

import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "TestimonialResponse",
		description = "This holds testimonial data. It is used to transfer testimonial data to a client."
)
public class TestimonialResponse {

	@ApiModelProperty("Identifier of a testimonial.")
	private String id;
	@ApiModelProperty("Author of a testimonial.")
	private String author;
	@ApiModelProperty("Testimonial comment.")
	private String comment;
	@ApiModelProperty("The datetime when testimonial was created.")
	private LocalDateTime creationDatetime;
	@ApiModelProperty("The datetime when testimonial was last edited.")
	private LocalDateTime lastModifiedDatetime;
	@ApiModelProperty("The skills linked to the testimonial.")
	private Set<SkillResponse> skills;

	public static TestimonialResponse of(Testimonial testimonial) {
		return TestimonialResponse.builder()
				.id(testimonial.getId())
				.author(testimonial.getAuthor())
				.comment(testimonial.getComment())
				.creationDatetime(testimonial.getCreationDatetime())
				.lastModifiedDatetime(testimonial.getLastModifiedDatetime())
				.skills(convertSkillListToSkillResponseSet(testimonial.getSkills()))
				.build();
	}

}
