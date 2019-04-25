package com.tsmms.skoop.skill;

import com.tsmms.skoop.skill.query.SkillQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillQueryServiceTests {
	@Mock
	private SkillRepository skillRepository;
	private SkillQueryService skillQueryService;

	@BeforeEach
	void setUp() {
		skillQueryService = new SkillQueryService(skillRepository);
	}

	@Test
	@DisplayName("Returns a stream of all skills from the data repository")
	void returnsStreamOfAllSkills() {
		given(skillRepository.findAll()).willReturn(Arrays.asList(
				Skill.builder().id("34").name("Spring Boot").description("Java Application Framework").build(),
				Skill.builder().id("12").name("Angular").description("JavaScript Framework").build()));

		Stream<Skill> skills = skillQueryService.getSkills();

		assertThat(skills).isNotNull();
		List<Skill> skillList = skills.collect(toList());
		assertThat(skillList).hasSize(2);
		Skill skill = skillList.get(0);
		assertThat(skill.getId()).isEqualTo("34");
		assertThat(skill.getName()).isEqualTo("Spring Boot");
		assertThat(skill.getDescription()).isEqualTo("Java Application Framework");
		skill = skillList.get(1);
		assertThat(skill.getId()).isEqualTo("12");
		assertThat(skill.getName()).isEqualTo("Angular");
		assertThat(skill.getDescription()).isEqualTo("JavaScript Framework");
	}

	@DisplayName("Converts skills names to skills.")
	@Test
	void convertSkillNamesToSkills() {
		when(skillRepository.findByNameIgnoreCase(anyString())).thenAnswer(invocation -> {
			final Object name = invocation.getArgument(0);
			if ("Angular".equals(name)) {
				return Optional.empty();
			} else if ("Spring Boot".equals(name)) {
				return Optional.of(
						Skill.builder()
								.id("123")
								.name("Spring Boot")
								.build()
				);
			} else {
				return null;
			}
		});
		final List<Skill> skills = skillQueryService.convertSkillNamesToSkillsList(Arrays.asList("Angular", "Spring Boot"));
		assertThat(skills).containsExactlyInAnyOrder(
				Skill.builder()
						.id("123")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.name("Angular")
						.build()
		);
	}

	@DisplayName("Empty collection is returned when empty collection is passed to convert skill names to skills.")
	@Test
	void emptyCollectionIsReturnedWhenEmptyCollectionIsPassedToConvertSkillNamesToSkills() {
		assertThat(skillQueryService.convertSkillNamesToSkillsList(Collections.emptyList())).isEmpty();
	}

}
