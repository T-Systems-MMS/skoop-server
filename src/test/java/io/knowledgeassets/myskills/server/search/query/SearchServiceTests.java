package io.knowledgeassets.myskills.server.search.query;

import io.knowledgeassets.myskills.server.search.AnonymousUserSkillRepository;
import io.knowledgeassets.myskills.server.search.AnonymousUserSkillResult;
import io.knowledgeassets.myskills.server.search.UserSearchSkillCriterion;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static java.util.stream.Collectors.toList;

@ExtendWith(MockitoExtension.class)
class SearchServiceTests {

	@Mock
	private AnonymousUserSkillRepository anonymousUserSkillRepository;

	private SearchService searchService;

	@BeforeEach
	void setUp() {
		this.searchService = new SearchService(anonymousUserSkillRepository);
	}

	@Test
	@DisplayName("Tests if anonymous user skills are fetched.")
	void testGettingAnonymousSkills() {

		given(anonymousUserSkillRepository.findAnonymousUserSkillsBySkillLevels(Collections.singletonList(UserSearchSkillCriterion.builder()
				.skillId("B")
				.minimumCurrentLevel(2)
				.build()))).willReturn(Stream.of(AnonymousUserSkillResult.builder()
						.referenceId("ref1")
						.userSkills(Arrays.asList(
								UserSkill.builder()
										.currentLevel(1)
										.skill(Skill.builder()
												.name("Angular")
												.build())
										.build(),
								UserSkill.builder()
										.currentLevel(2)
										.skill(Skill.builder()
												.name("Spring Boot")
												.build())
										.build()
								)
						)
						.build()
				)
		);

		Stream<AnonymousUserSkillResult> result = searchService.findAnonymousUserSkillsBySkillLevel(
				Collections.singletonList(UserSearchSkillCriterion.builder()
						.skillId("B")
						.minimumCurrentLevel(2)
						.build())
		);

		List<AnonymousUserSkillResult> res = result.collect(toList());
		assertThat(res).isNotNull();
		assertThat(res).hasSize(1);
		AnonymousUserSkillResult anonymousUserSkillResult = res.get(0);
		assertThat(anonymousUserSkillResult.getReferenceId()).isEqualTo("ref1");
		List<UserSkill> userSkills = anonymousUserSkillResult.getUserSkills();
		assertThat(userSkills).hasSize(2);
		final UserSkill firstUserSkill = userSkills.get(0);
		assertThat(firstUserSkill.getCurrentLevel()).isEqualTo(1);
		assertThat(firstUserSkill.getSkill().getName()).isEqualTo("Angular");
		final UserSkill secondUserSkill = userSkills.get(1);
		assertThat(secondUserSkill.getCurrentLevel()).isEqualTo(2);
		assertThat(secondUserSkill.getSkill().getName()).isEqualTo("Spring Boot");
	}

	@Test
	@DisplayName("Tests if an exception is thrown when there are no parameters to search by")
	void testIfExceptionIsThrownWhenThereAreNoParametersToSearchBy() {
		assertThrows(IllegalArgumentException.class, () -> searchService.findAnonymousUserSkillsBySkillLevel(Collections.emptyList()));
	}

}
