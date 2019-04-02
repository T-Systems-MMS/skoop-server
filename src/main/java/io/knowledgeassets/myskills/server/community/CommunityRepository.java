package io.knowledgeassets.myskills.server.community;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CommunityRepository extends Neo4jRepository<Community, String>, RecommendedCommunityRepository {

	@Query("MATCH (community:Community) WHERE TOLOWER(community.title) = TOLOWER({title}) RETURN community")
	Optional<Community> findByTitleIgnoreCase(@Param("title") String title);

	@Query("MATCH (:Community {id:{communityId}})-[:COMMUNITY_USER {role:'MANAGER'}]-(user:User {id:{userId}}) " +
			"RETURN COUNT(user) > 0")
	Boolean isCommunityManager(@Param("userId") String userId, @Param("communityId") String communityId);

	@Query("MATCH (:Community {id:{communityId}})-[:COMMUNITY_USER]-(user:User {id:{userId}}) " +
			"RETURN COUNT(user) > 0")
	Boolean isCommunityMember(@Param("userId") String userId, @Param("communityId") String communityId);

	@Query("MATCH (u:Community)-[:COMMUNITY_USER]-(user:User {id:{userId}}) " +
			"RETURN u")
	Stream<Community> getUserCommunities(@Param("userId") String userId);

}
