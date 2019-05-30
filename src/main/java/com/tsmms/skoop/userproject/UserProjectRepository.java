package com.tsmms.skoop.userproject;

import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserProjectRepository extends Neo4jRepository<UserProject, Long> {

	Optional<UserProject> findByUserIdAndProjectId(String userId, String projectId);

	Iterable<UserProject> findByUserId(String userId);

	@Depth(2)
	Stream<UserProject> findByApprovedIsFalse();

}
