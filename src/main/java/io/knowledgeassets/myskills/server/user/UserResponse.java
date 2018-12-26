package io.knowledgeassets.myskills.server.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(
		value = "UserResponse",
		description = "This holds information of a user. It will be used for sending user information to client."
)
public class UserResponse {
	@ApiModelProperty("User id")
	private String id;

	@ApiModelProperty("UserName of the user. It can not be null.")
	private String userName;

	@ApiModelProperty("First name of the user.")
	private String firstName;

	@ApiModelProperty("Last name of the user.")
	private String lastName;

	@ApiModelProperty("Email of the user.")
	private String email;

	@ApiModelProperty("Show as coach?")
	private Boolean coach;

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

	public static UserResponse of(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.coach(user.getCoach())
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
