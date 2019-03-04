package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityRepository;
import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.AllOf.allOf;

@DataNeo4jTest
class CommunityUserRepositoryTests {

	@Autowired
	private CommunityUserRepository communityUserRepository;

	@Autowired
	private CommunityRepository communityRepository;

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

		assertThat(result).isNotEmpty();
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
						.user(tester)
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

		assertThat(result).isNotEmpty();
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
				.userName("tester")
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
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(anotherTester)
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
						.build(),
				CommunityUser.builder()
						.community(dotNetUserGroup)
						.user(anotherTester)
						.role(CommunityRole.MANAGER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build(),
				CommunityUser.builder()
						.community(dotNetUserGroup)
						.user(anotherTester)
						.role(CommunityRole.MEMBER)
						.creationDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 15, 20, 0))
						.build()
		));
		final Stream<CommunityUser> communityUserStream = communityUserRepository.findByCommunityId("123");
		final List<CommunityUser> communityUserList = communityUserStream.collect(Collectors.toList());
		assertThat(communityUserList).hasSize(4);
		assertThat(communityUserList).containsExactlyInAnyOrder(
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
				.userName("tester")
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
						.build(),
				CommunityUser.builder()
						.community(javaUserGroup)
						.user(anotherTester)
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
		assertThat(communityUserList).hasSize(2);
		assertThat(communityUserList).containsExactlyInAnyOrder(
				IteratorUtils.toList(communityUsers.iterator()).stream()
						.filter(cu -> "123".equals(cu.getCommunity().getId()) && CommunityRole.MANAGER.equals(cu.getRole())).toArray(CommunityUser[]::new));

	}

}
