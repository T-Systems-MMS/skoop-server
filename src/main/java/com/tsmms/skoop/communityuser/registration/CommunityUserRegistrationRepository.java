package com.tsmms.skoop.communityuser.registration;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CommunityUserRegistrationRepository extends Neo4jRepository<CommunityUserRegistration, String> {

	Optional<CommunityUserRegistration> findByRegisteredUserIdAndCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull(String registeredUserId, String communityId);

	Stream<CommunityUserRegistration> findByCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull(String communityId);

	Stream<CommunityUserRegistration> findByCommunityIdAndApprovedByUserIsNullAndApprovedByCommunityIsTrue(String communityId);

}
