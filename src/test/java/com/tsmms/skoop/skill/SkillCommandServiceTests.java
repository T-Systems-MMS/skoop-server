package com.tsmms.skoop.skill;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.skillgroup.SkillGroup;
import com.tsmms.skoop.skillgroup.query.SkillGroupQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

@ExtendWith(MockitoExtension.class)
class SkillCommandServiceTests {
	@Mock
	private SkillRepository skillRepository;
	@Mock
	private SkillGroupQueryService skillGroupQueryService;
	private SkillCommandService skillCommandService;

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
	@DisplayName("Create skill with a skill group")
	void createSkillWithSkillGroup() {
		given(skillGroupQueryService.findByNameIgnoreCase("programming languages")).willReturn(
				Optional.of(SkillGroup.builder()
						.id("123")
						.name("programming languages")
						.build()
				)
		);
		given(skillRepository.findByNameIgnoreCase("Java")).willReturn(Optional.empty());
		given(skillRepository.save(argThat(allOf(
				isA(Skill.class),
				hasProperty("name", is("Java")),
				hasProperty("description", is("A programming language")),
				hasProperty("skillGroups", equalTo(Collections.singletonList(
						SkillGroup.builder()
								.id("123")
								.name("programming languages")
								.build()
				)))
				))
		))
				.willReturn(Skill.builder().id("34")
						.name("Java")
						.description("A programming language")
						.skillGroups(Collections.singletonList(
								SkillGroup.builder()
										.id("123")
										.name("programming languages")
										.build()
						))
						.build());

		Skill skill = skillCommandService.createSkill("Java", "A programming language", Collections.singletonList("programming languages"));

		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isNotNull();
		assertThat(skill.getId()).isEqualTo("34");
		assertThat(skill.getName()).isEqualTo("Java");
		assertThat(skill.getDescription()).isEqualTo("A programming language");
		assertThat(skill.getSkillGroups()).hasSize(1);
		assertThat(skill.getSkillGroups().get(0).getName()).isEqualTo("programming languages");
		assertThat(skill.getSkillGroups().get(0).getId()).isEqualTo("123");
	}

	@Test
	@DisplayName("Create Skill that has already exist")
	void createSkillThrowsException() {
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

	@Test
	@DisplayName("Update skill with a skill group")
	void updateSkillWithSkillGroup() {

		given(skillGroupQueryService.findByNameIgnoreCase("frameworks")).willReturn(
				Optional.of(SkillGroup.builder()
						.id("456")
						.name("frameworks")
						.build()
				)
		);

		given(skillRepository.findById("123"))
				.willReturn(Optional.of(Skill.builder().id("123").name("Spring Boot").build()));
		given(skillRepository.save(
				argThat(allOf(
						isA(Skill.class),
						hasProperty("name", is("Spring Boot")),
						hasProperty("description", is("Java Application Framework")),
						hasProperty("skillGroups", equalTo(Collections.singletonList(
								SkillGroup.builder()
										.id("456")
										.name("frameworks")
										.build()
						)))
				))
		))
				.willReturn(Skill.builder()
						.id("123")
						.name("Spring Boot")
						.description("Java Application Framework")
						.skillGroups(Collections.singletonList(
								SkillGroup.builder()
										.id("456")
										.name("frameworks")
										.build()
						))
						.build());

		Skill skill = skillCommandService.updateSkill("123", "Spring Boot", "Java Application Framework", Collections.singletonList("frameworks"));

		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isNotNull();
		assertThat(skill.getId()).isEqualTo("123");
		assertThat(skill.getName()).isEqualTo("Spring Boot");
		assertThat(skill.getDescription()).isEqualTo("Java Application Framework");
		assertThat(skill.getSkillGroups()).hasSize(1);
		assertThat(skill.getSkillGroups().get(0).getName()).isEqualTo("frameworks");
		assertThat(skill.getSkillGroups().get(0).getId()).isEqualTo("456");
	}

	@Test
	@DisplayName("Update non existent skill throws an exception")
	void updateNonExistentSkillThrowsException() {
		given(skillRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () ->
				skillCommandService.updateSkill("123", "Spring Boot", "Java Application Framework", Collections.singletonList("frameworks")));
	}

	@Test
	@DisplayName("Delete non existent skill throws an exception")
	void deleteNonExistentSkillThrowsException() {
		given(skillRepository.findById("123")).willReturn(Optional.empty());
		assertThrows(NoSuchResourceException.class, () -> skillCommandService.deleteSkill("123"));
	}

	@Test
	@DisplayName("Creates non existent skills.")
	void createsNonExistentSkills() {
		given(skillRepository.findByNameIgnoreCase("Angular")).willReturn(Optional.empty());

		given(skillRepository.save(
				argThat(allOf(
						isA(Skill.class),
						hasProperty("id", isA(String.class)),
						hasProperty("name", is("Angular"))
				))
		)).willReturn(
				Skill.builder()
					.id("456")
					.name("Angular")
				.build()
		);

		final List<Skill> skills = new ArrayList<>(skillCommandService.createNonExistentSkills(Arrays.asList(
				Skill.builder()
						.id("123")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.name("Angular")
						.build()
		)));
		assertThat(skills).containsExactlyInAnyOrder(
				Skill.builder()
						.id("123")
						.name("Spring Boot")
						.build(),
				Skill.builder()
						.id("456")
						.name("Angular")
						.build()
		);
	}

	@DisplayName("Empty collection is returned when empty collection is passed to create non existent skills.")
	@Test
	void emptyCollectionIsReturnedWhenEmptyCollectionIsPassedToCreateNonExistentSkills() {
		final List<Skill> skills = new ArrayList<>(skillCommandService.createNonExistentSkills(Collections.emptyList()));
		assertThat(skills).isEmpty();
	}

}
