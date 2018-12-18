package io.knowledgeassets.myskills.server.skill;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class SkillRepositoryTests {
	@Autowired
	private SkillRepository skillRepository;

	@Test
	@DisplayName("Provides the existing skill queried by its exact name")
	void providesSkillByExactName() {
		// Given
		skillRepository.save(Skill.builder().id("123").name("Angular").description("JavaScript Framework").build());
		// When
		Optional<Skill> skill = skillRepository.findByNameIgnoreCase("Angular");
		// Then
		assertThat(skill).isNotEmpty();
		assertThat(skill.get().getId()).isEqualTo("123");
		assertThat(skill.get().getName()).isEqualTo("Angular");
		assertThat(skill.get().getDescription()).isEqualTo("JavaScript Framework");
	}

	@Test
	@DisplayName("Provides the existing skill queried by its name ignoring case")
//	@Disabled("SDN 5.0.x does not support 'IgnoreCase' in finder methods yet. " +
//			"This will be implemented with SDN 5.1, " +
//			"see https://docs.spring.io/spring-data/neo4j/docs/5.1.0.RC2/reference/html/#_query_methods")
	void providesSkillByNameIgnoringCase() {
		// Given
		skillRepository.save(Skill.builder().id("123").name("Angular").description("JavaScript Framework").build());
		// When
		Optional<Skill> skill = skillRepository.findByNameIgnoreCase("anGulaR");
		// Then
		assertThat(skill).isNotEmpty();
		assertThat(skill.get().getId()).isEqualTo("123");
		assertThat(skill.get().getName()).isEqualTo("Angular");
		assertThat(skill.get().getDescription()).isEqualTo("JavaScript Framework");
	}

	@Test
	@DisplayName("Indicates that the skill with a given name exists ignoring case")
	void indicatesExistingSkillByNameIgnoringCase() {
		// Given
		skillRepository.save(Skill.builder().id("123").name("Angular").description("JavaScript Framework").build());
		// When
		Boolean skillExists = skillRepository.isSkillExistByNameIgnoreCase("anGulaR");
		// Then
		assertThat(skillExists).isTrue();
	}

	@Test
	@DisplayName("Indicates that the skill with a given name does not exist")
	void indicatesNonExistingSkillByName() {
		// Given
		skillRepository.save(Skill.builder().id("123").name("Angular").description("JavaScript Framework").build());
		// When
		Boolean skillExists = skillRepository.isSkillExistByNameIgnoreCase("spring");
		// Then
		assertThat(skillExists).isFalse();
	}
}
