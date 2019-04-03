package com.tsmms.skoop.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.SkillRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.stream.Collectors.toList;

@DataNeo4jTest
class AnonymousUserSkillRepositoryTests {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserSkillRepository userSkillRepository;

	private AnonymousUserSkillRepository anonymousUserSkillRepository;

	@BeforeEach
	void setUp() {

		this.anonymousUserSkillRepository = new AnonymousUserSkillRepositoryImpl(sessionFactory, new ObjectMapper());

		// Given
		Skill angular = Skill.builder()
				.id("A")
				.name("Angular")
				.description("JavaScript Framework")
				.build();
		angular = skillRepository.save(angular);
		Skill springBoot = Skill.builder()
				.id("B")
				.name("Spring Boot")
				.description("Java Application Framework")
				.build();
		springBoot = skillRepository.save(springBoot);
		Skill springSecurity = Skill.builder()
				.id("C")
				.name("Spring Security")
				.description("Java Security Framework")
				.build();
		springSecurity = skillRepository.save(springSecurity);

		User tester = User.builder()
				.id("1")
				.referenceId("ref1")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);
		User other = User.builder()
				.id("2")
				.referenceId("ref2")
				.userName("other")
				.build();
		other = userRepository.save(other);

		UserSkill testerAngular = UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build();
		userSkillRepository.save(testerAngular);
		UserSkill testerSpringBoot = UserSkill.builder()
				.user(tester)
				.skill(springBoot)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build();
		userSkillRepository.save(testerSpringBoot);
		userSkillRepository.save(UserSkill.builder()
				.user(other)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(other)
				.skill(springSecurity)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(other)
				.skill(springBoot)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build());
	}

	@Test
	@DisplayName("Tests if anonymous user skills are fetched by one parameter")
	void testGettingAnonymousUserSkillsByOneParameter() {
		Stream<AnonymousUserSkillResult> result = anonymousUserSkillRepository.findAnonymousUserSkillsBySkillLevels(Collections.singletonList(
				UserSearchSkillCriterion.builder()
						.skillId("B")
						.minimumCurrentLevel(2)
						.build()
		));
		assertThat(result).isNotNull();
		List<AnonymousUserSkillResult> anonymousUserSkillResults = result.collect(toList());
		assertThat(anonymousUserSkillResults).hasSize(1);
		final AnonymousUserSkillResult res = anonymousUserSkillResults.get(0);
		assertThat(res).isNotNull();
		assertThat(res.getReferenceId()).isEqualTo("ref1");
		final List<UserSkill> userSkills = anonymousUserSkillResults.get(0).getUserSkills();
		assertThat(userSkills).hasSize(1);
		final UserSkill userSkill = userSkills.get(0);
		assertThat(userSkill).isNotNull();
		assertThat(userSkill.getCurrentLevel()).isEqualTo(2);
		final Skill skill = userSkill.getSkill();
		assertThat(skill).isNotNull();
		assertThat(skill.getName()).isEqualTo("Spring Boot");
	}

	@Test
	@DisplayName("Tests if anonymous user skills are fetched by two parameters")
	void testGettingAnonymousUserSkillsByTwoParameters() {
		Stream<AnonymousUserSkillResult> result = anonymousUserSkillRepository.findAnonymousUserSkillsBySkillLevels(Arrays.asList(
				UserSearchSkillCriterion.builder()
						.skillId("B")
						.minimumCurrentLevel(2)
						.build(),
				UserSearchSkillCriterion.builder()
						.skillId("A")
						.minimumCurrentLevel(1)
						.build()
		));
		assertThat(result).isNotNull();
		List<AnonymousUserSkillResult> anonymousUserSkillResults = result.collect(toList());
		assertThat(anonymousUserSkillResults).hasSize(1);
		final AnonymousUserSkillResult res = anonymousUserSkillResults.get(0);
		assertThat(res).isNotNull();
		assertThat(res.getReferenceId()).isEqualTo("ref1");
		final List<UserSkill> userSkills = anonymousUserSkillResults.get(0).getUserSkills();
		assertThat(userSkills).hasSize(2);
		UserSkill userSkill = userSkills.get(0);
		assertThat(userSkill).isNotNull();
		assertThat(userSkill.getCurrentLevel()).isEqualTo(1);
		Skill skill = userSkill.getSkill();
		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isEqualTo("A");
		assertThat(skill.getName()).isEqualTo("Angular");
		userSkill = userSkills.get(1);
		assertThat(userSkill).isNotNull();
		assertThat(userSkill.getCurrentLevel()).isEqualTo(2);
		skill = userSkill.getSkill();
		assertThat(skill).isNotNull();
		assertThat(skill.getId()).isEqualTo("B");
		assertThat(skill.getName()).isEqualTo("Spring Boot");
	}

}
