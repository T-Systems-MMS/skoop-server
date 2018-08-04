package io.knowledgeassets.myskills.server.skill.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataNeo4jTest
class SkillQueryRepositoryTests {
	@Autowired
	private SkillQueryRepository skillQueryRepository;

	@Test
	@DisplayName("Provides the existing skill queried by its name")
	void providesSkillByName() {
		// Given
		skillQueryRepository.save(new Skill().id("123").name("Angular").description("JavaScript Framework"));
		// When
		Optional<Skill> skill = skillQueryRepository.findByName("Angular");
		// Then
		assertThat(skill).isNotEmpty();
		assertThat(skill.get().getId()).isEqualTo("123");
		assertThat(skill.get().getName()).isEqualTo("Angular");
		assertThat(skill.get().getDescription()).isEqualTo("JavaScript Framework");
	}
}
