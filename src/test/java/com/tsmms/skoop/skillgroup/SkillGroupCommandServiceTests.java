package com.tsmms.skoop.skillgroup;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.skillgroup.command.SkillGroupCommandService;
import com.tsmms.skoop.skillgroup.query.SkillGroupQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SkillGroupCommandServiceTests {
	@Mock
	private SkillGroupRepository skillGroupRepository;
	private SkillGroupCommandService skillGroupCommandService;
	private SkillGroupQueryService skillGroupQueryService;

	@BeforeEach
	void setUp() {
		skillGroupCommandService = new SkillGroupCommandService(skillGroupRepository);
	}

	@Test
	@DisplayName("Create skill group")
	void createSkillGroup() {
		given(skillGroupRepository.findByNameIgnoreCase("Programming")).willReturn(Optional.empty());
		given(skillGroupRepository.save(ArgumentMatchers.isA(SkillGroup.class)))
				.willReturn(SkillGroup.builder().id("34").name("Programming").description("programming languages group").build());

		SkillGroup skillGroup = skillGroupCommandService.createGroup("Programming", "programming languages group");

		assertThat(skillGroup).isNotNull();
		assertThat(skillGroup.getId()).isNotNull();
		assertThat(skillGroup.getId()).isEqualTo("34");
		assertThat(skillGroup.getName()).isEqualTo("Programming");
		assertThat(skillGroup.getDescription()).isEqualTo("programming languages group");
	}

	@Test
	@DisplayName("Create skill group that has already exist")
	void createSkillGroup_ThrowsException() {
		given(skillGroupRepository.findByNameIgnoreCase("Programming")).willReturn(Optional.of(
				SkillGroup.builder().id("34").name("Programming").description("programming languages group").build()));

		assertThrows(DuplicateResourceException.class, () -> {
			skillGroupCommandService.createGroup("Programming", "programming languages group");
		});
	}

	@Test
	@DisplayName("Update Skill group")
	void updateSkillGroup() {
		given(skillGroupRepository.findById("123"))
				.willReturn(Optional.of(SkillGroup.builder().id("123").name("Programming").build()));
		given(skillGroupRepository.save(ArgumentMatchers.any(SkillGroup.class)))
				.willReturn(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());

		SkillGroup skillGroup = skillGroupCommandService.updateGroup("123", "Programming", "programming languages group");

		assertThat(skillGroup).isNotNull();
		assertThat(skillGroup.getId()).isNotNull();
		assertThat(skillGroup.getId()).isEqualTo("123");
		assertThat(skillGroup.getName()).isEqualTo("Programming");
		assertThat(skillGroup.getDescription()).isEqualTo("programming languages group");
	}

}
