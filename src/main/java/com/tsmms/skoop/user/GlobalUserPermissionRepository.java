package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface GlobalUserPermissionRepository extends Neo4jRepository<GlobalUserPermission, String> {

	Long deleteByOwnerId(String ownerId);

	Stream<GlobalUserPermission> findByOwnerId(String ownerId);

	Stream<GlobalUserPermission> findByOwnerIdAndScope(String ownerId, GlobalUserPermissionScope scope);

	@Query("MATCH (u:User)-[r:HAS_GRANTED]->(gup:GlobalUserPermission) " +
			"WHERE u.id <> {userId} " +
			"RETURN gup, r, u")
	Iterable<GlobalUserPermission> getInboundGlobalUserPermissions(@Param("userId") String userId);

	@Query("MATCH (u:User)-[r:HAS_GRANTED]->(gup:GlobalUserPermission {scope:{scope}}) " +
			"WHERE u.id <> {userId} " +
			"RETURN gup, r, u")
	Iterable<GlobalUserPermission> getInboundGlobalUserPermissionsByScope(@Param("userId") String userId, @Param("scope") GlobalUserPermissionScope scope);

	@Query("MATCH (:User {id:{ownerId}})-[r:HAS_GRANTED]->(:GlobalUserPermission {scope:{scope}}) RETURN COUNT(r) > 0")
	Boolean isGlobalPermissionGranted(String ownerId, GlobalUserPermissionScope scope);

}
