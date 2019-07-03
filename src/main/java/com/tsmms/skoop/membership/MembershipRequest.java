package com.tsmms.skoop.membership;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "MembershipRequest",
		description = "Request object to create or update membership."
)
public class MembershipRequest {

	@ApiModelProperty("Membership ID.")
	private String id;
	@ApiModelProperty("Name of an organisation / web-site / blog the membership is associated with.")
	@NotEmpty
	private String name;
	@ApiModelProperty("Additional information.")
	private String description;
	@ApiModelProperty("Link to organisation / web-site / blog.")
	private String link;
	@ApiModelProperty("The start date.")
	private LocalDate startDate;
	@ApiModelProperty("The end date.")
	private LocalDate endDate;
	@ApiModelProperty("Skills linked to the membership.")
	private List<String> skills;

}
