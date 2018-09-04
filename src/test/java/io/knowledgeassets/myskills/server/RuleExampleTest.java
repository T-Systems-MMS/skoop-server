package io.knowledgeassets.myskills.server;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

//@ExtendWith(TraceUnitExtension.class)
@ExtendWith({SpringExtension.class})
@DataNeo4jTest
public class RuleExampleTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private UserSkillRepository userSkillRepository;

	@Test
	public void whenTracingTests() {
		// Given
		Skill angular = Skill.builder().id("12").name("Angular").description("JavaScript Framework").build();
		Skill springBoot = Skill.builder().id("34").name("Spring Boot").description("Java Application Framework").build();
		Skill springSecurity = Skill.builder().id("56").name("Spring Security").description("Java Security Framework").build();
		Skill springData = Skill.builder().id("78").name("Spring Data").description("Java Data Access Framework").build();

		skillRepository.save(angular);
		skillRepository.save(springBoot);
		skillRepository.save(springSecurity);
		skillRepository.save(springData);
		User user = createUser1(angular, springBoot, springSecurity);
		User user2 = createUser2(angular, springBoot, springSecurity, springData);
		userRepository.save(user);
		userRepository.save(user2);
	}

	private User createUser1(Skill angular, Skill springBoot, Skill springSecurity) {
		User user = User.builder().id("123").userName("tester1").build();

		List<UserSkill> userSkills = new ArrayList<>();
		userSkills.add(UserSkill.builder().id("123;12").user(user).skill(angular)
				.currentLevel(1).desiredLevel(3).priority(2).build()
		);
		userSkills.add(UserSkill.builder().id("123;34").user(user).skill(springBoot)
				.currentLevel(2).desiredLevel(3).priority(4).build()
		);
		userSkills.add(UserSkill.builder().id("123;56").user(user).skill(springSecurity)
				.currentLevel(2).desiredLevel(4).priority(0).build()
		);
		user.setUserSkills(userSkills);
		return user;
	}

	private User createUser2(Skill angular, Skill springBoot, Skill springSecurity, Skill springData) {
		User user = User.builder().id("456").userName("tester2").build();

		List<UserSkill> userSkills = new ArrayList<>();
		userSkills.add(UserSkill.builder().id("456;12").user(user).skill(angular)
				.currentLevel(2).desiredLevel(4).priority(1).build()
		);
		userSkills.add(UserSkill.builder().id("456;34").user(user).skill(springBoot)
				.currentLevel(1).desiredLevel(3).priority(4).build()
		);
		userSkills.add(UserSkill.builder().id("456;56").user(user).skill(springSecurity)
				.currentLevel(3).desiredLevel(4).priority(2).build()
		);
		userSkills.add(UserSkill.builder().id("456;78").user(user).skill(springData)
				.currentLevel(3).desiredLevel(3).priority(0).build()
		);
		user.setUserSkills(userSkills);
		return user;
	}
}
