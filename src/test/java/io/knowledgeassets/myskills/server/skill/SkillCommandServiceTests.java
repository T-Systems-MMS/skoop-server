package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.skillgroup.query.SkillGroupQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SkillCommandServiceTests {
	@Mock
	private SkillRepository skillRepository;
	private SkillCommandService skillCommandService;
	private SkillGroupQueryService skillGroupQueryService;

	@BeforeEach
	void setUp() {
		skillCommandService = new SkillCommandService(skillRepository, skillGroupQueryService);
	}

	@Test
	@DisplayName("Create Skill without skill group")
	void createSkillWithoutSkillGroup() {
		given(skillRepository.findByNameIgnoreCase("Java")).willReturn(Optional.empty());
		given(skillRepository.save(ArgumentMatchers.isA(Skill.class)))
				.willReturn(Skill.builder().id("34").name("Java").description("A programming language").build());

		Skill skill = skillCommandService.createSkill("Java", "A programming language", List.of());

		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isNotNull();
		assertThat(skill.getId()).isEqualTo("34");
		assertThat(skill.getName()).isEqualTo("Java");
		assertThat(skill.getDescription()).isEqualTo("A programming language");
	}

	@Test
	@DisplayName("Create Skill that has already exist")
	void createSkill_ThrowsException() {
		given(skillRepository.findByNameIgnoreCase("Java")).willReturn(Optional.of(
				Skill.builder().id("34").name("Java").description("A programming language").build()));

		assertThrows(DuplicateResourceException.class, () -> {
			skillCommandService.createSkill("Java", "A programming language", List.of());
		});
	}

	@Test
	@DisplayName("Update Skill")
	void updateSkill() {
		given(skillRepository.findById("123"))
				.willReturn(Optional.of(Skill.builder().id("123").name("Spring Boot").build()));
		given(skillRepository.save(ArgumentMatchers.any(Skill.class)))
				.willReturn(Skill.builder().id("123").name("Spring Boot").description("Java Application Framework").build());

		Skill skill = skillCommandService.updateSkill("123", "Spring Boot", "Java Application Framework", List.of());

		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isNotNull();
		assertThat(skill.getId()).isEqualTo("123");
		assertThat(skill.getName()).isEqualTo("Spring Boot");
		assertThat(skill.getDescription()).isEqualTo("Java Application Framework");
	}

}
