package com.tsmms.skoop.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class PermissionResponse {

	@ApiModelProperty("ID of the permission.")
	private String id;

	@ApiModelProperty("Owning user who grants the permission.")
	private UserSimpleResponse owner;

}
