package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface GlobalPermissionRepository extends Neo4jRepository<GlobalPermission, String> {

	Long deleteByOwnerId(String ownerId);

	Stream<GlobalPermission> findByOwnerId(String ownerId);

	@Query("MATCH (:User {id:{ownerId}})-[r:HAS_GRANTED]->(:GlobalPermission {scope:{scope}}) RETURN COUNT(r) > 0")
	Boolean isGlobalPermissionGranted(String ownerId, UserPermissionScope scope);

}
