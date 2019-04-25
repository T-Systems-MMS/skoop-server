package com.tsmms.skoop.publication;

import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "PublicationResponse",
		description = "This holds publication data. It is used to transfer publication data to a client."
)
public class PublicationResponse {

	@ApiModelProperty("Identifier of a publication.")
	private String id;
	@ApiModelProperty("Title of the publication.")
	private String title;
	@ApiModelProperty("Date when the publication was brought out.")
	private LocalDate date;
	@ApiModelProperty("Publisher or location of a conference.")
	private String publisher;
	@ApiModelProperty("Link to the publication.")
	private String link;
	@ApiModelProperty("The datetime when publication was created.")
	private LocalDateTime creationDatetime;
	@ApiModelProperty("The datetime when publication was last edited.")
	private LocalDateTime lastModifiedDatetime;
	@ApiModelProperty("The skills linked to the publication.")
	private Set<SkillResponse> skills;

	public static PublicationResponse of(Publication publication) {
		return PublicationResponse.builder()
				.id(publication.getId())
				.title(publication.getTitle())
				.date(publication.getDate())
				.publisher(publication.getPublisher())
				.link(publication.getLink())
				.creationDatetime(publication.getCreationDatetime())
				.lastModifiedDatetime(publication.getLastModifiedDatetime())
				.skills(convertSkillListToSkillResponseSet(publication.getSkills()))
				.build();
	}

}
