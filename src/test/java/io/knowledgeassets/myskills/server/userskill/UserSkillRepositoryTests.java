package io.knowledgeassets.myskills.server.userskill;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
	@DisplayName("Provides the skills related to the user given by user ID")
	void providesSkillsRelatedToGivenUserId() {
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
				.userName("tester")
				.build();
		tester = userRepository.save(tester);
		User other = User.builder()
				.id("2")
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
				.skill(springBoot)
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

		// When
		Iterable<UserSkill> userSkills = userSkillRepository.findByUserId("1");

		// Then
		assertThat(userSkills).hasSize(2);
		assertThat(userSkills).containsExactlyInAnyOrder(testerAngular, testerSpringBoot);
	}

	@Test
	@DisplayName("Provides the users related to the skill given by skill ID")
	void providesUsersRelatedToGivenSkillId() {
		// Given
		User developer = User.builder()
				.id("1")
				.userName("developer")
				.build();
		developer = userRepository.save(developer);
		User tester = User.builder()
				.id("2")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);
		User other = User.builder()
				.id("3")
				.userName("other")
				.build();
		other = userRepository.save(other);

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

		UserSkill angularDeveloper = UserSkill.builder()
				.user(developer)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build();
		userSkillRepository.save(angularDeveloper);
		UserSkill angularTester = UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build();
		userSkillRepository.save(angularTester);
		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(springBoot)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(other)
				.skill(springBoot)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build());

		// When
		Iterable<UserSkill> userSkills = userSkillRepository.findBySkillId("A");

		// Then
		assertThat(userSkills).hasSize(2);
		assertThat(userSkills).containsExactlyInAnyOrder(angularDeveloper, angularTester);
	}

	@Test
	@DisplayName("Provides the users related to the skill given by skill ID with at least the given priority")
	void providesUsersRelatedToGivenSkillIdWithAtLeastGivenPriority() {
		// Given
		User developer = User.builder()
				.id("1")
				.userName("developer")
				.build();
		developer = userRepository.save(developer);
		User tester = User.builder()
				.id("2")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);

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

		UserSkill angularDeveloper = UserSkill.builder()
				.user(developer)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(1)
				.build();
		userSkillRepository.save(angularDeveloper);
		UserSkill angularTester = UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(2)
				.priority(0)
				.build();
		userSkillRepository.save(angularTester);
		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(springBoot)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build());

		// When
		Iterable<UserSkill> userSkills = userSkillRepository.findBySkillIdAndPriorityGreaterThanEqual("A", 1);

		// Then
		assertThat(userSkills).hasSize(1);
		assertThat(userSkills).containsExactlyInAnyOrder(angularDeveloper);
	}

	@Test
	@DisplayName("Provides the users suitable as coach for the given user ID and skill ID")
	void providesCoachesForGivenUserIdAndSkillId() {
		// Given
		User developer = User.builder()
				.id("1")
				.userName("developer")
				.build();
		developer = userRepository.save(developer);
		User tester = User.builder()
				.id("2")
				.userName("tester")
				.coach(true)
				.build();
		tester = userRepository.save(tester);
		User coach = User.builder()
				.id("3")
				.userName("coach")
				.coach(true)
				.build();
		coach = userRepository.save(coach);
		User noCoach = User.builder()
				.id("4")
				.userName("nocoach")
				.build();
		noCoach = userRepository.save(noCoach);

		Skill angular = Skill.builder()
				.id("A")
				.name("Angular")
				.description("JavaScript Framework")
				.build();
		angular = skillRepository.save(angular);

		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(1)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(coach)
				.skill(angular)
				.currentLevel(3)
				.desiredLevel(3)
				.priority(0)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(noCoach)
				.skill(angular)
				.currentLevel(3)
				.desiredLevel(4)
				.priority(1)
				.build());

		// When
		Iterable<User> coaches = userSkillRepository.findCoachesByUserIdAndSkillId("1", "A");

		// Then
		assertThat(coaches).hasSize(1);
		assertThat(coaches).containsExactlyInAnyOrder(coach);
	}

	@Test
	@DisplayName("Provides the top 10 of the skills with the highest average priority")
	void providesTop10PrioritizedSkills() {
		// Given
		User developer = User.builder()
				.id("1")
				.userName("developer")
				.build();
		developer = userRepository.save(developer);
		User tester = User.builder()
				.id("2")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);
		User consultant = User.builder()
				.id("3")
				.userName("consultant")
				.build();
		consultant = userRepository.save(consultant);

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
		Skill scrum = Skill.builder()
				.id("C")
				.name("Scrum")
				.description("Agile Project Management")
				.build();
		scrum = skillRepository.save(scrum);

		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(angular)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(springBoot)
				.currentLevel(3)
				.desiredLevel(3)
				.priority(0)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(springBoot)
				.currentLevel(2)
				.desiredLevel(2)
				.priority(0)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(developer)
				.skill(scrum)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(1)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(scrum)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(2)
				.build());
		userSkillRepository.save(UserSkill.builder()
				.user(consultant)
				.skill(scrum)
				.currentLevel(3)
				.desiredLevel(4)
				.priority(3)
				.build());

		// When
		Iterable<UserSkillPriorityAggregationResult> prioritizedSkills = userSkillRepository.findTop10PrioritizedSkills();

		// Then
		assertThat(prioritizedSkills).hasSize(2);
		assertThat(prioritizedSkills).containsExactly(
				UserSkillPriorityAggregationResult.builder()
						.skill(angular)
						.averagePriority(3.5)
						.maximumPriority(4.0)
						.userCount(2)
						.build(),
				UserSkillPriorityAggregationResult.builder()
						.skill(scrum)
						.averagePriority(2.0)
						.maximumPriority(3.0)
						.userCount(3)
						.build()
		);
	}

	@Test
	@DisplayName("Provides the skills not related to the user and containing the search term")
	void providesSkillSuggestionsByUserId() {
		// Given
		Skill angular = Skill.builder()
				.id("A")
				.name("Angular")
				.description("JavaScript Framework")
				.build();
		skillRepository.save(angular);
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
		Skill springData = Skill.builder()
				.id("D")
				.name("Spring Data")
				.description("Java Data Access Framework")
				.build();
		springData = skillRepository.save(springData);

		User tester = User.builder()
				.id("1")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);

		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(springBoot)
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build());

		// When
		Iterable<Skill> skills = userSkillRepository.findSkillSuggestionsByUserId("1", "spr");

		// Then
		assertThat(skills).hasSize(2);
		assertThat(skills).containsExactly(springData, springSecurity);
	}
}
