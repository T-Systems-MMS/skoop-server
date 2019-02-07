package io.knowledgeassets.myskills.server.community;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRepository extends Neo4jRepository<Community, String> {

	@Query("MATCH (community:Community) WHERE TOLOWER(community.title) = TOLOWER({title}) RETURN community")
	Optional<Community> findByTitleIgnoreCase(@Param("title") String title);

}