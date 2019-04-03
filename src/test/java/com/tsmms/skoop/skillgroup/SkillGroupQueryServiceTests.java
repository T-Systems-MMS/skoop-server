package com.tsmms.skoop.skillgroup;

import com.tsmms.skoop.skillgroup.query.SkillGroupQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SkillGroupQueryServiceTests {
	@Mock
	private SkillGroupRepository skillGroupRepository;
	private SkillGroupQueryService skillGroupQueryService;

	@BeforeEach
	void setUp() {
		skillGroupQueryService = new SkillGroupQueryService(skillGroupRepository);
	}

	@Test
	@DisplayName("Returns a stream of all skill groups from the data repository")
	void returnsStreamOfAllSkillGroups() {
		given(skillGroupRepository.findAll()).willReturn(Arrays.asList(
				SkillGroup.builder().id("34").name("Programming").description("programming languages group").build(),
				SkillGroup.builder().id("12").name("Web").description("web Framework group").build()));

		Stream<SkillGroup> skillGroups = skillGroupQueryService.getSkillGroups();

		Assertions.assertThat(skillGroups).isNotNull();
		List<SkillGroup> skillGroupList = skillGroups.collect(toList());
		Assertions.assertThat(skillGroupList).hasSize(2);
		SkillGroup skillGroup = skillGroupList.get(0);
		assertThat(skillGroup.getId()).isEqualTo("34");
		assertThat(skillGroup.getName()).isEqualTo("Programming");
		assertThat(skillGroup.getDescription()).isEqualTo("programming languages group");
		skillGroup = skillGroupList.get(1);
		assertThat(skillGroup.getId()).isEqualTo("12");
		assertThat(skillGroup.getName()).isEqualTo("Web");
		assertThat(skillGroup.getDescription()).isEqualTo("web Framework group");
	}
}
