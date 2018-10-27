package io.knowledgeassets.myskills.server.user;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends Neo4jRepository<UserPermission, String> {
	Iterable<UserPermission> findByOwnerId(String ownerId);

	Long deleteByOwnerId(String ownerId);
}
