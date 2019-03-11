package io.knowledgeassets.myskills.server.communityuser.registration;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityUserRegistrationRepository extends Neo4jRepository<CommunityUserRegistration, String> {

	Optional<CommunityUserRegistration> findByRegisteredUserIdAndCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull(String registeredUserId, String communityId);

}
