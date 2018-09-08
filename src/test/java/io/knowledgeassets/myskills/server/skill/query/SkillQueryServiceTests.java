package io.knowledgeassets.myskills.server.skill.query;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
}
