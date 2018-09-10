package io.knowledgeassets.myskills.server.report;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitializeDataForReport {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private UserSkillRepository userSkillRepository;

	/**
	 * It create 4 skills and 2 users and assign skills to users with different values.
	 */
	 void createData() {
		Skill angular = Skill.builder().id("12").name("Angular").description("JavaScript Framework").build();
		Skill springBoot = Skill.builder().id("34").name("Spring Boot").description("Java Application Framework").build();
		Skill springSecurity = Skill.builder().id("56").name("Spring Security").description("Java Security Framework").build();
		Skill springData = Skill.builder().id("78").name("Spring Data").description("Java Data Access Framework").build();

		skillRepository.save(angular);
		skillRepository.save(springBoot);
		skillRepository.save(springSecurity);
		skillRepository.save(springData);
		createUser1(angular, springBoot, springSecurity);
		createUser2(angular, springBoot, springSecurity, springData);
	}

	private void createUser1(Skill angular, Skill springBoot, Skill springSecurity) {
		User user = User.builder().id("123").userName("tester1").build();
		userRepository.save(user);

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
		userSkillRepository.saveAll(userSkills);
	}

	private void createUser2(Skill angular, Skill springBoot, Skill springSecurity, Skill springData) {
		User user = User.builder().id("456").userName("tester2").build();
		userRepository.save(user);

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
		userSkillRepository.saveAll(userSkills);
	}
}
