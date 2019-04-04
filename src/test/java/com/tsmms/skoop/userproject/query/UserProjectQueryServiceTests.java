package com.tsmms.skoop.userproject.query;

import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserProjectQueryServiceTests {

	@Mock
	private UserProjectRepository userProjectRepository;

	private UserProjectQueryService userProjectQueryService;

	@BeforeEach
	void setUp() {
		userProjectQueryService = new UserProjectQueryService(userProjectRepository);
	}

	@Test
	@DisplayName("Retrieves user-project relationships by user id")
	void retrievesUserProjectRelationshipsByUserId() {
		given(userProjectRepository.findByUserId("123")).willReturn(
				Arrays.asList(
						UserProject.builder()
								.role("developer")
								.tasks("development")
								.id(732L)
								.build(),
						UserProject.builder()
								.role("designer")
								.tasks("design")
								.id(733L)
								.build()
				)
		);

		Stream<UserProject> userProjects = userProjectQueryService.getUserProjects("123");

		assertThat(userProjects).isNotNull();
		List<UserProject> userProjectsList = userProjects.collect(toList());
		assertThat(userProjectsList).hasSize(2);
		UserProject userProject = userProjectsList.get(0);
		assertThat(userProject.getId()).isEqualTo(732L);
		assertThat(userProject.getRole()).isEqualTo("developer");
		assertThat(userProject.getTasks()).isEqualTo("development");
		userProject = userProjectsList.get(1);
		assertThat(userProject.getId()).isEqualTo(733L);
		assertThat(userProject.getRole()).isEqualTo("designer");
		assertThat(userProject.getTasks()).isEqualTo("design");
	}

	@Test
	@DisplayName("Retrieves user-project relationships by user id")
	void retrievesUserProjectRelationshipsByUserIdAndProjectId() {
		given(userProjectRepository.findByUserIdAndProjectId("123", "ABC")).willReturn(
				Optional.of(UserProject.builder()
						.role("developer")
						.tasks("development")
						.id(732L)
						.build())
		);

		Optional<UserProject> userProject = userProjectQueryService.getUserProjectByUserIdAndProjectId("123", "ABC");

		assertThat(userProject).isPresent();
		assertThat(userProject.get().getRole()).isEqualTo("developer");
		assertThat(userProject.get().getTasks()).isEqualTo("development");
		assertThat(userProject.get().getId()).isEqualTo(732L);
	}

}
