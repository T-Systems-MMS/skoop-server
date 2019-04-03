package com.tsmms.skoop.userproject;

import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.ProjectRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class UserProjectRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserProjectRepository userProjectRepository;

	@Test
	@DisplayName("Retrieves user-project relationship by user id")
	void retrievesUserProjectRelationshipByUserId() {
		// Given
		Project firstProject = Project.builder()
				.id("1")
				.name("First project")
				.description("First project description")
				.build();
		firstProject = projectRepository.save(firstProject);
		Project secondProject = Project.builder()
				.id("2")
				.name("Second project")
				.description("Second project description")
				.build();
		secondProject = projectRepository.save(secondProject);
		Project thirdProject = Project.builder()
				.id("3")
				.name("Third project")
				.description("Third project description")
				.build();
		thirdProject = projectRepository.save(thirdProject);

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

		UserProject testerFirstProject = UserProject.builder()
				.user(tester)
				.project(firstProject)
				.role("QA")
				.tasks("testing")
				.build();
		userProjectRepository.save(testerFirstProject);
		UserProject testerThirdProject = UserProject.builder()
				.user(tester)
				.project(thirdProject)
				.role("QA")
				.tasks("testing")
				.build();
		userProjectRepository.save(testerThirdProject);
		userProjectRepository.save(UserProject.builder()
				.user(other)
				.project(firstProject)
				.role("Developer")
				.tasks("Development")
				.build());
		userProjectRepository.save(UserProject.builder()
				.user(other)
				.project(secondProject)
				.role("Developer")
				.tasks("Development")
				.build());

		// When
		Iterable<UserProject> userSkills = userProjectRepository.findByUserId("1");

		// Then
		Assertions.assertThat(userSkills).hasSize(2);
		Assertions.assertThat(userSkills).containsExactlyInAnyOrder(testerFirstProject, testerThirdProject);
	}

	@Test
	@DisplayName("Retrieves user-project relationship by user id and project id")
	void retrievesRelationshipByUserIdAndProjectId() {
		Project project = Project.builder()
				.id("ABC")
				.name("Project")
				.description("Project description")
				.build();
		projectRepository.save(project);
		User user = User.builder()
				.id("123")
				.userName("tester")
				.build();
		userRepository.save(user);
		UserProject testerProject = UserProject.builder()
				.user(user)
				.project(project)
				.role("QA")
				.tasks("testing")
				.build();
		userProjectRepository.save(testerProject);

		Optional<UserProject> userProject = userProjectRepository.findByUserIdAndProjectId("123", "ABC");

		Assertions.assertThat(userProject).isPresent();
		assertThat(userProject.get().getRole()).isEqualTo("QA");
		assertThat(userProject.get().getTasks()).isEqualTo("testing");
		assertThat(userProject.get().getUser()).isEqualTo(user);
		assertThat(userProject.get().getProject()).isEqualTo(project);
	}

}
