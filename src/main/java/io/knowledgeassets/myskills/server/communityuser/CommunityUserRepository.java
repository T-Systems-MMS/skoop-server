package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.community.CommunityRole;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CommunityUserRepository extends Neo4jRepository<CommunityUser, Long> {

	Optional<CommunityUser> findByUserIdAndCommunityId(String userId, String communityId);

	Optional<CommunityUser> findByUserIdAndCommunityIdAndRole(String userId, String communityId, CommunityRole role);

	Stream<CommunityUser> findByCommunityId(String communityId);

	Stream<CommunityUser> findByCommunityIdAndRole(String communityId, CommunityRole role);

}
