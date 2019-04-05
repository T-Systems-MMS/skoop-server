package com.tsmms.skoop.community;

import com.tsmms.skoop.community.link.Link;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "LinkResponse",
		description = "This holds link data. It is used to transfer link data to a client."
)
public class LinkResponse {

	@ApiModelProperty("Link ID.")
	private Long id;
	@ApiModelProperty("Name of a link.")
	private String name;
	@ApiModelProperty("Hyper reference of a link.")
	private String href;

	public static List<LinkResponse> convertLinkListToLinkResponseList(List<Link> links) {
		if (links == null) {
			return Collections.emptyList();
		}
		return links.stream().map(l -> LinkResponse.builder()
				.id(l.getId())
				.name(l.getName())
				.href(l.getHref())
				.build()
		).collect(toList());
	}

}
