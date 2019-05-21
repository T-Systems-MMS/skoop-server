package com.tsmms.skoop.userskill;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tsmms.skoop.notification.AbstractNotificationResponse;
import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "UserSkillsEstimationNotificationResponse",
		description = "This holds notification data that a user has to estimate her / his skills. " +
				"It is used to transfer user skill estimation data to a client."
)
@JsonTypeName("UserSkillsEstimationNotification")
public class UserSkillsEstimationNotificationResponse extends AbstractNotificationResponse {

	@ApiModelProperty("Skills to estimate.")
	private Set<SkillResponse> skills;

	@Builder
	public UserSkillsEstimationNotificationResponse(String id, LocalDateTime creationDatetime, Set<SkillResponse> skills) {
		super(id, creationDatetime);
		this.skills = skills;
	}


	public static UserSkillsEstimationNotificationResponse of(UserSkillsEstimationNotification notification) {
		return UserSkillsEstimationNotificationResponse.builder()
				.id(notification.getId())
				.creationDatetime(notification.getCreationDatetime())
				.skills(convertSkillListToSkillResponseSet(notification.getSkills()))
				.build();
	}

}
