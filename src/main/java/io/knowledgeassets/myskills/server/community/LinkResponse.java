package io.knowledgeassets.myskills.server.community;

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
		value = "LinkResponse",
		description = "This holds link data. It is used to transfer link data to a client."
)
public class LinkResponse {

	@ApiModelProperty("Name of a link.")
	private String name;
	@ApiModelProperty("Hyper reference of a link.")
	private String href;

}
