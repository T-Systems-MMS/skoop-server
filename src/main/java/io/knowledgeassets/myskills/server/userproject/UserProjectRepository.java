package io.knowledgeassets.myskills.server.userproject;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProjectRepository extends Neo4jRepository<UserProject, Long> {

	Optional<UserProject> findByUserIdAndProjectId(String userId, String projectId);

	Iterable<UserProject> findByUserId(String userId);

}
