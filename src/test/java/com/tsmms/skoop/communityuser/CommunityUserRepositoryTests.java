package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.SkillRepository;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.UserSkillRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class CommunityUserRepositoryTests {

	@Autowired
	private CommunityUserRepository communityUserRepository;

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserSkillRepository userSkillRepository;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("Find community user by user ID and community ID.")
	@Test
	void findCommunityUserByUserIdAndCommunityId() {

		User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.save(CommunityUser.builder()
				.community(javaUserGroup)
				.user(tester)
				.role(CommunityRole.MEMBER)
				.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.build()
		);

		Community dotNetUserGroup = communityRepository.save(Community.builder()
				.id("456")
				.title(".NET User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.save(CommunityUser.builder()
				.community(dotNetUserGroup)
				.user(tester)
				.role(CommunityRole.MEMBER)
				.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
				.build()
		);

		Optional<CommunityUser> result = communityUserRepository.findByUserIdAndCommunityId("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123");

		Assertions.assertThat(result).isNotEmpty();
		CommunityUser communityUser = result.get();
		assertThat(communityUser.getId()).isNotNull();
		assertThat(communityUser.getUser()).isEqualTo(tester);
		assertThat(communityUser.getCommunity()).isEqualTo(javaUserGroup);
		assertThat(communityUser.getRole()).isEqualTo(CommunityRole.MEMBER);
		assertThat(communityUser.getCreationDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(communityUser.getLastModifiedDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
	}

	@DisplayName("Find community user by user ID, community ID and role.")
	@Test
	void findCommunityUserByUserIdAndCommunityIdAndRole() {
		User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(tester)
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(tester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));

		Community dotNetUserGroup = communityRepository.save(Community.builder()
				.id("456")
				.title(".NET User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(dotNetUserGroup)
						.user(User.builder()
								.id("abc")
								.userName("commonUser")
								.build())
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(dotNetUserGroup)
						.user(tester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));

		Optional<CommunityUser> result = communityUserRepository.findByUserIdAndCommunityIdAndRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123", CommunityRole.MANAGER);

		Assertions.assertThat(result).isNotEmpty();
		CommunityUser communityUser = result.get();
		assertThat(communityUser.getId()).isNotNull();
		assertThat(communityUser.getUser()).isEqualTo(tester);
		assertThat(communityUser.getCommunity()).isEqualTo(javaUserGroup);
		assertThat(communityUser.getRole()).isEqualTo(CommunityRole.MANAGER);
		assertThat(communityUser.getCreationDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
		assertThat(communityUser.getLastModifiedDate()).isEqualTo(LocalDateTime.of(2019, 1, 15, 20, 0));
	}

	@DisplayName("Find community user list by community ID.")
	@Test
	void findCommunityUserListByCommunityId() {

		User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());

		User anotherTester = userRepository.save(User.builder()
				.id("2edc1229-b4d0-4119-9113-4677beb20ae2")
				.userName("anotherTester")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		Community dotNetUserGroup = communityRepository.save(Community.builder()
				.id("456")
				.title(".NET User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		Iterable<CommunityUser> communityUsers = communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(tester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(anotherTester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(dotNetUserGroup)
						.user(anotherTester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));
		final Stream<CommunityUser> communityUserStream = communityUserRepository.findByCommunityId("123");
		final List<CommunityUser> communityUserList = communityUserStream.collect(Collectors.toList());
		Assertions.assertThat(communityUserList).hasSize(2);
		Assertions.assertThat(communityUserList).containsExactlyInAnyOrder(
				IteratorUtils.toList(communityUsers.iterator()).stream().filter(cu -> "123".equals(cu.getCommunity().getId())).toArray(CommunityUser[]::new));
	}

	@DisplayName("Find community user list by community ID and role.")
	@Test
	void findByCommunityIdAndRole() {
		User tester = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build());

		User anotherTester = userRepository.save(User.builder()
				.id("2edc1229-b4d0-4119-9113-4677beb20ae2")
				.userName("anotherTester")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		Iterable<CommunityUser> communityUsers = communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(tester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(User.builder()
								.id("abc")
								.userName("commonUser")
								.build())
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(anotherTester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));
		final Stream<CommunityUser> communityUserStream = communityUserRepository.findByCommunityIdAndRole("123", CommunityRole.MANAGER);
		final List<CommunityUser> communityUserList = communityUserStream.collect(Collectors.toList());
		Assertions.assertThat(communityUserList).hasSize(2);
		Assertions.assertThat(communityUserList).containsExactlyInAnyOrder(
				IteratorUtils.toList(communityUsers.iterator()).stream()
						.filter(cu -> "123".equals(cu.getCommunity().getId()) && CommunityRole.MANAGER.equals(cu.getRole())).toArray(CommunityUser[]::new));

	}

	@DisplayName("Gets users not related to community.")
	@Test
	void getsUsersNotRelatedToCommunity() {
		User firstUser = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("firstUser")
				.build());

		User secondUser = userRepository.save(User.builder()
				.id("2edc1229-b4d0-4119-9113-4677beb20ae2")
				.userName("secondUser")
				.build());

		User thirdUser = userRepository.save(User.builder()
				.id("c20f6dac-ed8b-46f7-be31-3dcab1810c36")
				.userName("thirdUser")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.saveAll(Arrays.asList(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(firstUser)
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(thirdUser)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()));

		final Iterable<User> users = communityUserRepository.getUsersNotRelatedToCommunity("123", null);
		assertThat(users).hasSize(1);
		assertThat(users).containsExactly(secondUser);
	}

	@DisplayName("Gets users not related to community filtered by search parameter.")
	@Test
	void getsUsersNotRelatedToCommunityAndFilteredBySearchString() {
		// Not related user with "Doe" in the lastname
		User firstUser = userRepository.save(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("firstUser")
				.firstName("John")
				.lastName("Doe The First")
				.build());

		// Related user to the community with "Doe" in the surname
		User secondUser = userRepository.save(User.builder()
				.id("2edc1229-b4d0-4119-9113-4677beb20ae2")
				.userName("secondUser")
				.firstName("John")
				.lastName("Doe The Second")
				.build());

		// Not related user with "Doe" in the firstname
		User thirdUser = userRepository.save(User.builder()
				.id("c20f6dac-ed8b-46f7-be31-3dcab1810c36")
				.userName("thirdUser")
				.firstName("John Doe")
				.build());

		// Not related user with "doe" in the username
		User fourthUser = userRepository.save(User.builder()
				.id("4392f841-c797-400f-93bf-0d1adc0d125e")
				.userName("johndoe")
				.firstName("John")
				.build());

		// Not related user without "Doe"
		User fifthUser = userRepository.save(User.builder()
				.id("bb1a4150-62f5-4e9c-ae29-7d2566624645")
				.userName("tester")
				.firstName("tester")
				.lastName("tester")
				.build());

		Community javaUserGroup = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
				.description("Community for Java developers")
				.build()
		);

		communityUserRepository.save(
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(secondUser)
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build());

		final Iterable<User> users = communityUserRepository.getUsersNotRelatedToCommunity("123", "dOe");
		assertThat(users).hasSize(3);
		assertThat(users).containsExactlyInAnyOrder(firstUser, thirdUser, fourthUser);
	}

	@DisplayName("Gets users recommended to be invited to join a community.")
	@Test
	void getUsersRecommendedToBeInvitedToJoinCommunity() {

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
		User commonUser = User.builder()
				.id("d49357af-d88a-47aa-a381-15044065574b")
				.userName("commonUser")
				.build();

		UserSkill testerAngular = UserSkill.builder()
				.user(tester)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(3)
				.build();
		userSkillRepository.save(testerAngular);
		UserSkill commonUserAngular = UserSkill.builder()
				.user(commonUser)
				.skill(angular)
				.currentLevel(1)
				.desiredLevel(2)
				.priority(2)
				.build();
		userSkillRepository.save(commonUserAngular);
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

		Community javaUserGroup = communityRepository.save(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.description("Group for Java developers")
						.skills(Arrays.asList(springBoot, angular))
						.type(CommunityType.OPEN)
						.build()
		);

		Community frontendDevelopers = communityRepository.save(
				Community.builder()
						.id(UUID.randomUUID().toString())
						.title("Frontend developers")
						.description("Group for frontend developers")
						.skills(singletonList(angular))
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

		final Iterable<User> users = communityUserRepository.getUsersRecommendedToBeInvitedToJoinCommunity("123");
		assertThat(users).containsExactlyInAnyOrder(tester, commonUser);
	}

}
