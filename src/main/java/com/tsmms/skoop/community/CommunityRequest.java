package com.tsmms.skoop.community;

import com.tsmms.skoop.community.link.LinkRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "CommunityRequest",
		description = "Request object to create or update community."
)
public class CommunityRequest {

	@ApiModelProperty("Title of a community.")
	@NotEmpty
	private String title;

	@ApiModelProperty("Type of a community.")
	private CommunityType type;

	@ApiModelProperty("Description of a community.")
	private String description;

	@ApiModelProperty("Links of a community.")
	private List<LinkRequest> links;

	@ApiModelProperty("List of skill names.")
	private List<String> skillNames;

	@ApiModelProperty("List of identifiers of invited users.")
	private List<String> invitedUserIds;

}
