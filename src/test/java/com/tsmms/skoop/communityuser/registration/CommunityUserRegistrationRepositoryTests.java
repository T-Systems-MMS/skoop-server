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
import java.util.Optional;

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

}
