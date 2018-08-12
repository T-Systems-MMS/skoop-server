package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataNeo4jTest
class UserSkillRepositoryTests {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private UserSkillRepository userSkillRepository;

	@Test
	@DisplayName("Provides the skills not related to the user and containing the search term")
	void providesSkillSuggestionsByUserId() {
		// Given
		Skill angular = Skill.builder().id("12").name("Angular").description("JavaScript Framework").build();
		Skill springBoot = Skill.builder().id("34").name("Spring Boot").description("Java Application Framework").build();
		Skill springSecurity = Skill.builder().id("56").name("Spring Security").description("Java Security Framework").build();
		Skill springData = Skill.builder().id("78").name("Spring Data").description("Java Data Access Framework").build();
		skillRepository.save(angular);
		skillRepository.save(springBoot);
		skillRepository.save(springSecurity);
		skillRepository.save(springData);
		User user = User.builder().id("123").userName("tester").build();
		user.setUserSkills(singletonList(UserSkill.builder().id("123;34").user(user).skill(springBoot)
				.currentLevel(2).desiredLevel(3).priority(4).build()));
		userRepository.save(user);
		// When
		Iterable<Skill> skills = userSkillRepository.findSkillSuggestionsByUserId("123", "spr");
		// Then
		assertThat(skills).hasSize(2);
		Iterator<Skill> skillIterator = skills.iterator();
		Skill skill = skillIterator.next();
		assertThat(skill.getId()).isEqualTo("78");
		assertThat(skill.getName()).isEqualTo("Spring Data");
		assertThat(skill.getDescription()).isEqualTo("Java Data Access Framework");
		skill = skillIterator.next();
		assertThat(skill.getId()).isEqualTo("56");
		assertThat(skill.getName()).isEqualTo("Spring Security");
		assertThat(skill.getDescription()).isEqualTo("Java Security Framework");
	}
}
