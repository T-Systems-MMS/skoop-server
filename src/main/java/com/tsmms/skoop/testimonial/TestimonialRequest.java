package com.tsmms.skoop.testimonial;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "TestimonialRequest",
		description = "Request object to create or update testimonial."
)
public class TestimonialRequest {

	@ApiModelProperty("Testimonial ID.")
	private String id;
	@ApiModelProperty("Author of a testimonial.")
	@NotEmpty
	private String author;
	@ApiModelProperty("Comment.")
	@NotEmpty
	private String comment;
	@ApiModelProperty("Skills linked to the testimonial.")
	private Set<String> skills;

}
