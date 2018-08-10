package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryRepository;
import io.knowledgeassets.myskills.server.user.query.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryRepository;
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
class UserSkillQueryRepositoryTests {
	@Autowired
	private UserQueryRepository userQueryRepository;
	@Autowired
	private SkillQueryRepository skillQueryRepository;
	@Autowired
	private UserSkillQueryRepository userSkillQueryRepository;

	@Test
	@DisplayName("Provides the skills not related to the user and containing the search term")
	void providesSkillSuggestionsByUserId() {
		// Given
		Skill angular = new Skill().id("12").name("Angular").description("JavaScript Framework");
		Skill springBoot = new Skill().id("34").name("Spring Boot").description("Java Application Framework");
		Skill springSecurity = new Skill().id("56").name("Spring Security").description("Java Security Framework");
		Skill springData = new Skill().id("78").name("Spring Data").description("Java Data Access Framework");
		skillQueryRepository.save(angular);
		skillQueryRepository.save(springBoot);
		skillQueryRepository.save(springSecurity);
		skillQueryRepository.save(springData);
		User user = new User().id("123").userName("tester");
		user.userSkills(singletonList(new UserSkill().id("123;34").user(user).skill(springBoot)
				.currentLevel(2).desiredLevel(3).priority(4)));
		userQueryRepository.save(user);
		// When
		Iterable<Skill> skills = userSkillQueryRepository.findSkillSuggestionsByUserId("123", "spr");
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
