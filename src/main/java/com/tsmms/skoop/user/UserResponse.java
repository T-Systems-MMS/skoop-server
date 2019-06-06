package com.tsmms.skoop.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(
		value = "UserResponse",
		description = "This holds extended information of a user. It will be used for sending user information to client."
)
public class UserResponse extends UserSimpleResponse {

	@ApiModelProperty("Academic degree of the user.")
	private String academicDegree;

	@ApiModelProperty("Position profile of the user.")
	private String positionProfile;

	@ApiModelProperty("Summary.")
	private String summary;

	@ApiModelProperty("Industry sectors the user works (worked) in.")
	private List<String> industrySectors;

	@ApiModelProperty("Specializations which the user has.")
	private List<String> specializations;

	@ApiModelProperty("Certificates which the user has.")
	private List<String> certificates;

	@ApiModelProperty("Languages which the user knows.")
	private List<String> languages;

	@Builder
	public UserResponse(String id,
						String userName,
						String firstName,
						String lastName,
						String email,
						String academicDegree,
						String positionProfile,
						String summary,
						List<String> industrySectors,
						List<String> specializations,
						List<String> certificates,
						List<String> languages) {
		super(id, userName, firstName, lastName, email);
		this.academicDegree = academicDegree;
		this.positionProfile = positionProfile;
		this.summary = summary;
		this.industrySectors = industrySectors;
		this.specializations = specializations;
		this.certificates = certificates;
		this.languages = languages;
	}

	public static UserResponse of(User user) {
		if (user == null) {
			return null;
		}
		return UserResponse.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.academicDegree(user.getAcademicDegree())
				.positionProfile(user.getPositionProfile())
				.summary(user.getSummary())
				.industrySectors(user.getIndustrySectors())
				.specializations(user.getSpecializations())
				.certificates(user.getCertificates())
				.languages(user.getLanguages())
				.build();
	}
}
