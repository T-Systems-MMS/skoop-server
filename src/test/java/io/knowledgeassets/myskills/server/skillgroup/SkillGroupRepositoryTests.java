package io.knowledgeassets.myskills.server.skillgroup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class SkillGroupRepositoryTests {
	@Autowired
	private SkillGroupRepository skillGroupRepository;

	@Test
	@DisplayName("Provides the existing skill group queried by its exact name")
	void providesSkillGroupByExactName() {
		// Given
		skillGroupRepository.save(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());
		// When
		Optional<SkillGroup> skillGroup = skillGroupRepository.findByNameIgnoreCase("Programming");
		// Then
		assertThat(skillGroup).isNotEmpty();
		assertThat(skillGroup.get().getId()).isEqualTo("123");
		assertThat(skillGroup.get().getName()).isEqualTo("Programming");
		assertThat(skillGroup.get().getDescription()).isEqualTo("programming languages group");
	}

	@Test
	@DisplayName("Provides the existing skill group queried by its name ignoring case")
//	@Disabled("SDN 5.0.x does not support 'IgnoreCase' in finder methods yet. " +
//			"This will be implemented with SDN 5.1, " +
//			"see https://docs.spring.io/spring-data/neo4j/docs/5.1.0.RC2/reference/html/#_query_methods")
	void providesSkillGroupByNameIgnoringCase() {
		// Given
		skillGroupRepository.save(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());
		// When
		Optional<SkillGroup> skillGroup = skillGroupRepository.findByNameIgnoreCase("prOgraMMinG");
		// Then
		assertThat(skillGroup).isNotEmpty();
		assertThat(skillGroup.get().getId()).isEqualTo("123");
		assertThat(skillGroup.get().getName()).isEqualTo("Programming");
		assertThat(skillGroup.get().getDescription()).isEqualTo("programming languages group");
	}

	@Test
	@DisplayName("Indicates that the skill group with a given name exists ignoring case")
	void indicatesExistingSkillGroupByNameIgnoringCase() {
		// Given
		skillGroupRepository.save(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());
		// When
		Boolean skillGroupExists = skillGroupRepository.isSkillGroupExistByNameIgnoreCase("prOgraMMinG");
		// Then
		assertThat(skillGroupExists).isTrue();
	}

	@Test
	@DisplayName("Indicates that the skill with a given name does not exist")
	void indicatesNonExistingSkillGroupByName() {
		// Given
		skillGroupRepository.save(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());
		// When
		Boolean skillExists = skillGroupRepository.isSkillGroupExistByNameIgnoreCase("spring");
		// Then
		assertThat(skillExists).isFalse();
	}
}
