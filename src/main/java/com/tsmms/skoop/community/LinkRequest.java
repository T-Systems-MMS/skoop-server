package com.tsmms.skoop.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "LinkRequest",
		description = "Request object to create or update link."
)
public class LinkRequest {

	@ApiModelProperty("Link ID.")
	private Long id;
	@ApiModelProperty("Name of a link.")
	private String name;
	@ApiModelProperty("Hyper reference of a link.")
	private String href;

}
