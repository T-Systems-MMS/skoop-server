package com.tsmms.skoop.community;

import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.SkillRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.communityuser.CommunityUserRepository;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.UserSkillRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import static java.util.stream.Collectors.toList;

@DataNeo4jTest
class CommunityRepositoryTests {

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private CommunityUserRepository communityUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserSkillRepository userSkillRepository;

	@Test
	@DisplayName("Provides the existing project queried by its exact name")
	void providesCommunityByExactName() {

		// Given

		communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.links(Arrays.asList(
						Link.builder()
								.name("Facebook")
								.href("https://www.facebook.com/java-user-group")
								.build(),
						Link.builder()
								.name("Linkedin")
								.href("https://www.linkedin.com/java-user-group")
								.build()
				)).build());

		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("Java User Group");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPEN);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
	}

	@Test
	@DisplayName("Provides the existing project queried by its name ignoring case")
	void providesCommunityByNameIgnoringCase() {
		// Given
		communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.description("Community for Java developers")
						.links(Arrays.asList(
								Link.builder()
										.name("Facebook")
										.href("https://www.facebook.com/java-user-group")
										.build(),
								Link.builder()
										.name("Linkedin")
										.href("https://www.linkedin.com/java-user-group")
										.build()
						))
						.build()
		);
		// When
		Optional<Community> community = communityRepository.findByTitleIgnoreCase("jaVA uSeR grOUp");
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPEN);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
	}

	@Test
	@DisplayName("Saves community with skills")
	void saveCommunityWithSkills() {
		// Given
		Community c = communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
						.description("Community for Java developers")
						.skills(new HashSet<>(
								Arrays.asList(Skill.builder()
												.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
												.name("Spring Boot")
												.build(),
										Skill.builder()
												.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
												.name("Angular")
												.build())
						))
						.build()
		);
		// When
		Optional<Community> community = communityRepository.findById(c.getId());
		// Then
		assertThat(community).isNotEmpty();
		assertThat(community.get().getId()).isEqualTo("123");
		assertThat(community.get().getTitle()).isEqualTo("Java User Group");
		assertThat(community.get().getType()).isEqualTo(CommunityType.OPEN);
		assertThat(community.get().getDescription()).isEqualTo("Community for Java developers");
		Assertions.assertThat(community.get().getSkills()).contains(Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build(), Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build());
	}

	@DisplayName("Test if communities recommended for a user are retrieved.")
	@Test
	void testIfCommunitiesRecommendedForUserAreRetrieved() {

		Skill angular = Skill.builder()
				.id("323c89ae-8407-4ac1-8f79-89456e79a328")
				.name("Angular")
				.description("JavaScript Framework")
				.build();
		angular = skillRepository.save(angular);
		Skill springBoot = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.description("Java Application Framework")
				.build();
		springBoot = skillRepository.save(springBoot);
		Skill springSecurity = Skill.builder()
				.id("bd309237-80a2-44de-9552-1dfe760866d1")
				.name("Spring Security")
				.description("Java Security Framework")
				.build();
		springSecurity = skillRepository.save(springSecurity);
		Skill scala = Skill.builder()
				.id("6a3e27d3-ca20-498a-9121-844349b57bdc")
				.name("Scala")
				.description("Scala programming language.")
				.build();
		scala = skillRepository.save(scala);

		User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		tester = userRepository.save(tester);
		User other = User.builder()
				.id("f1228ec5-59ba-4ace-809b-1fbff60888d0")
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
		userSkillRepository.save(UserSkill.builder()
				.user(tester)
				.skill(scala)
				.currentLevel(1)
				.desiredLevel(1)
				.favourite(true)
				.priority(1)
				.build()
		);

		Community javaUserGroup = communityRepository.save(
				Community.builder()
						.id(UUID.randomUUID().toString())
						.title("Java User Group")
						.description("Group for Java developers")
						.skills(new HashSet<>(Arrays.asList(springBoot, angular)))
						.type(CommunityType.OPEN)
						.build()
		);

		Community scalaUserGroup = communityRepository.save(
				Community.builder()
						.id(UUID.randomUUID().toString())
						.title("Scala User Group")
						.description("Group for Scala developers")
						.skills(singleton(scala))
						.type(CommunityType.OPEN)
						.build()
		);

		Community frontendDevelopers = communityRepository.save(
				Community.builder()
						.id(UUID.randomUUID().toString())
						.title("Frontend developers")
						.description("Group for frontend developers")
						.skills(singleton(angular))
						.type(CommunityType.OPEN)
						.build()
		);

		Community dotnetDevelopers = communityRepository.save(
				Community.builder()
						.id(UUID.randomUUID().toString())
						.title(".NET developers")
						.description("Group for .NET developers")
						.type(CommunityType.OPEN)
						.build()
		);

		communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(other)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(frontendDevelopers)
						.user(other)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(frontendDevelopers)
						.user(tester)
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(dotnetDevelopers)
						.user(other)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));

		Stream<Community> communitiesStream = communityRepository.getRecommendedCommunities("1f37fb2a-b4d0-4119-9113-4677beb20ae2");

		List<Community> communities = communitiesStream.collect(toList());

		assertThat(communities).containsExactly(javaUserGroup, scalaUserGroup);

	}

	@DisplayName("Is user a community member?")
	@Test
	void isCommunityMember() {
		final User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		final Community javaUserGroup = communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Group for Java developers")
						.type(CommunityType.OPEN)
						.build()
		);
		communityUserRepository.save(CommunityUser.builder()
				.community(javaUserGroup)
				.user(tester)
				.role(CommunityRole.MEMBER)
				.build());
		assertThat(communityRepository.isCommunityMember("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).isTrue();
	}

	@DisplayName("Is user a community manager?")
	@Test
	void isCommunityManager() {
		final User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());
		final Community javaUserGroup = communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Group for Java developers")
						.type(CommunityType.OPEN)
						.build()
		);
		communityUserRepository.save(CommunityUser.builder()
				.community(javaUserGroup)
				.user(tester)
				.role(CommunityRole.MANAGER)
				.build());
		assertThat(communityRepository.isCommunityManager("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).isTrue();
	}

}
