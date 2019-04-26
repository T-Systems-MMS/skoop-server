package com.tsmms.skoop.publication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "PublicationRequest",
		description = "Request object to create or update publication."
)
public class PublicationRequest {

	@ApiModelProperty("Publication ID.")
	private String id;
	@ApiModelProperty("Title of the publication.")
	@NotEmpty
	private String title;
	@ApiModelProperty("Date when the publication was brought out.")
	private LocalDate date;
	@ApiModelProperty("Publisher or location of a conference.")
	@NotEmpty
	private String publisher;
	@ApiModelProperty("Link to the publication.")
	private String link;
	@ApiModelProperty("Skills linked to the publication.")
	private Set<String> skills;

}
