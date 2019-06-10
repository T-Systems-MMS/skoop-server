package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface GlobalUserPermissionRepository extends Neo4jRepository<GlobalUserPermission, String> {

	Long deleteByOwnerId(String ownerId);

	Stream<GlobalUserPermission> findByOwnerId(String ownerId);

	@Query("MATCH (u:User)-[r:HAS_GRANTED]->(gup:GlobalUserPermission {scope:{scope}}) " +
			"WHERE u.id <> {userId} " +
			"RETURN gup, r, u")
	Iterable<GlobalUserPermission> getGlobalUserPermissionsByScopeGrantedToUser(String userId, GlobalUserPermissionScope scope);

	@Query("MATCH (u:User)-[r:HAS_GRANTED]->(gup:GlobalUserPermission) " +
			"WHERE u.id <> {userId} " +
			"RETURN gup, r, u")
	Iterable<GlobalUserPermission> getGlobalUserPermissionsGrantedToUser(String userId);

	@Query("MATCH (:User {id:{ownerId}})-[r:HAS_GRANTED]->(:GlobalUserPermission {scope:{scope}}) RETURN COUNT(r) > 0")
	Boolean isGlobalPermissionGranted(String ownerId, GlobalUserPermissionScope scope);

}
