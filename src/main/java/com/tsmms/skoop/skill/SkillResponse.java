package com.tsmms.skoop.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "SkillResponse",
		description = "This holds information of a skill. It will be used for sending skill information to client."
)

public class SkillResponse {
	@ApiModelProperty("Skill id")
	private String id;

	@ApiModelProperty("Name of a skill. It cannot be blank.")
	private String name;

	@ApiModelProperty("Description of a skill.")
	private String description;

	@EqualsAndHashCode.Exclude
	private List<String> skillGroups;

	public static SkillResponse of(Skill skill) {
		return SkillResponse.builder()
				.id(skill.getId())
				.name(skill.getName())
				.description(skill.getDescription())
				.build();
	}

	private static <C extends Collection<SkillResponse>> Optional<C> convertSkillCollectionToSkillResponseCollection(Supplier<? extends C> collectionFactory, Collection<Skill> skills) {
		if (skills != null) {
			return Optional.of(skills.stream().map(SkillResponse::of).collect(Collectors.toCollection(collectionFactory)));
		}
		else {
			return Optional.empty();
		}
	}

	public static List<SkillResponse> convertSkillListToSkillResponseList(List<Skill> skills) {
		return SkillResponse.<List<SkillResponse>>convertSkillCollectionToSkillResponseCollection(ArrayList::new, skills).orElse(Collections.emptyList());
	}

	public static Set<SkillResponse> convertSkillListToSkillResponseSet(Set<Skill> skills) {
		return SkillResponse.<Set<SkillResponse>>convertSkillCollectionToSkillResponseCollection(HashSet::new, skills).orElse(Collections.emptySet());
	}

}
