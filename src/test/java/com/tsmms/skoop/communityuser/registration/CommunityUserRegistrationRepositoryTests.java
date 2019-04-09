package com.tsmms.skoop.communityuser.registration;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class CommunityUserRegistrationRepositoryTests {

	@Autowired
	private CommunityUserRegistrationRepository communityUserRegistrationRepository;

	@Autowired
	private CommunityRepository communityRepository;

	@Autowired
	private UserRepository userRepository;

	@DisplayName("Finds community user registration by community ID and registered user ID. " +
			"The community user registration has to be approved by user and unknown to be approved by community.")
	@Test
	void findsApprovedByUserRegistrationByCommunityIdAndRegisteredUserId() {

		Community community = communityRepository.save(Community.builder()
				.id("456")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build()
		);

		User user = userRepository.save(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.build());

		communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.registeredUser(user)
				.community(community)
				.approvedByUser(true)
				.approvedByCommunity(null)
				.creationDatetime(LocalDateTime.of(2019, 2, 20, 20, 0))
				.id("abc")
				.build()
		);

		Optional<CommunityUserRegistration> registrationOptional = communityUserRegistrationRepository
				.findByRegisteredUserIdAndCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull("db87d46a-e4ca-451a-903b-e8533e0b924b", "456");
		Assertions.assertThat(registrationOptional).isNotEmpty();
		CommunityUserRegistration registration = registrationOptional.get();
		assertThat(registration.getId()).isEqualTo("abc");
	}

	@DisplayName("Finds community user registrations for the specified community approved by community and still not approved by the user.")
	@Test
	void findsByCommunityIdAndApprovedByUserIsNullAndApprovedByCommunityIsTrue() {

		Community community = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build()
		);

		User user = userRepository.save(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.build());

		communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.registeredUser(user)
				.community(community)
				.approvedByUser(null)
				.approvedByCommunity(true)
				.creationDatetime(LocalDateTime.of(2019, 2, 20, 20, 0))
				.id("abc")
				.build()
		);

		communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.registeredUser(user)
				.community(community)
				.approvedByUser(true)
				.approvedByCommunity(true)
				.creationDatetime(LocalDateTime.of(2019, 2, 20, 20, 0))
				.id("def")
				.build()
		);

		final Stream<CommunityUserRegistration> communityUserRegistrationStream = communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsNullAndApprovedByCommunityIsTrue("123");
		final List<CommunityUserRegistration> communityUserRegistrations = communityUserRegistrationStream.collect(Collectors.toList());
		assertThat(communityUserRegistrations).hasSize(1);
		assertThat(communityUserRegistrations.get(0).getId()).isEqualTo("abc");
		assertThat(communityUserRegistrations.get(0).getApprovedByCommunity()).isTrue();
		assertThat(communityUserRegistrations.get(0).getApprovedByUser()).isNull();
		assertThat(communityUserRegistrations.get(0).getCommunity()).isEqualTo(community);
		assertThat(communityUserRegistrations.get(0).getRegisteredUser()).isEqualTo(user);
		assertThat(communityUserRegistrations.get(0).getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 2, 20, 20, 0));
	}

	@DisplayName("Finds community user registrations for the specified community approved by user and still not approved by the community.")
	@Test
	void findByCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull() {

		Community community = communityRepository.save(Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.CLOSED)
				.build()
		);

		User user = userRepository.save(User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("tester")
				.build());

		communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.registeredUser(user)
				.community(community)
				.approvedByUser(true)
				.approvedByCommunity(null)
				.creationDatetime(LocalDateTime.of(2019, 2, 20, 20, 0))
				.id("abc")
				.build()
		);

		communityUserRegistrationRepository.save(CommunityUserRegistration.builder()
				.registeredUser(user)
				.community(community)
				.approvedByUser(true)
				.approvedByCommunity(true)
				.creationDatetime(LocalDateTime.of(2019, 2, 20, 20, 0))
				.id("def")
				.build()
		);

		final Stream<CommunityUserRegistration> communityUserRegistrationStream = communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull("123");
		final List<CommunityUserRegistration> communityUserRegistrations = communityUserRegistrationStream.collect(Collectors.toList());
		assertThat(communityUserRegistrations).hasSize(1);
		assertThat(communityUserRegistrations.get(0).getId()).isEqualTo("abc");
		assertThat(communityUserRegistrations.get(0).getApprovedByCommunity()).isNull();
		assertThat(communityUserRegistrations.get(0).getApprovedByUser()).isTrue();
		assertThat(communityUserRegistrations.get(0).getCommunity()).isEqualTo(community);
		assertThat(communityUserRegistrations.get(0).getRegisteredUser()).isEqualTo(user);
		assertThat(communityUserRegistrations.get(0).getCreationDatetime()).isEqualTo(LocalDateTime.of(2019, 2, 20, 20, 0));
	}

}
