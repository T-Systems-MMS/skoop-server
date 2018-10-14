package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserSkillQueryServiceTests {
	@Mock
	private UserSkillRepository userSkillRepository;
	@Mock
	private UserQueryService userQueryService;
	@Mock
	private SkillQueryService skillQueryService;

	private UserSkillQueryService userSkillQueryService;

	@BeforeEach
	void prepareTest() {
		userSkillQueryService = new UserSkillQueryService(userSkillRepository, userQueryService, skillQueryService);
	}

	@Test
	@DisplayName("Provides the skills related to the user given by user ID")
	void providesSkillsRelatedToGivenUserId() {
		User tester = User.builder()
				.id("123")
				.userName("tester")
				.build();
		UserSkill testerAngular = UserSkill.builder()
				.id("123;ABC")
				.user(tester)
				.skill(Skill.builder()
						.id("ABC")
						.name("Angular")
						.description("JavaScript Framework")
						.build())
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build();
		UserSkill testerSpringBoot = UserSkill.builder()
				.id("123;DEF")
				.user(tester)
				.skill(Skill.builder()
						.id("DEF")
						.name("Spring Boot")
						.description("Java Application Framework")
						.build())
				.currentLevel(2)
				.desiredLevel(3)
				.priority(4)
				.build();
		given(userSkillRepository.findByUserId("123")).willReturn(asList(testerAngular, testerSpringBoot));

		Stream<UserSkill> userSkills = userSkillQueryService.getUserSkillsByUserId("123");

		assertThat(userSkills).containsExactly(testerAngular, testerSpringBoot);
	}

	@Test
	@DisplayName("Provides the existing relationship for the given user ID and skill ID")
	void providesRelationshipForGivenUserIdAndSkillId() {
		UserSkill testerAngular = UserSkill.builder()
				.id("123;ABC")
				.user(User.builder()
						.id("123")
						.userName("tester")
						.build())
				.skill(Skill.builder()
						.id("ABC")
						.name("Angular")
						.description("JavaScript Framework")
						.build())
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build();
		given(userSkillRepository.findByUserIdAndSkillId("123", "ABC")).willReturn(Optional.of(testerAngular));

		Optional<UserSkill> userSkill = userSkillQueryService.getUserSkillByUserIdAndSkillId("123", "ABC");

		assertThat(userSkill).isNotNull();
		assertThat(userSkill).contains(testerAngular);
	}
}
