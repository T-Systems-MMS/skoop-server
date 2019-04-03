package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends Neo4jRepository<UserPermission, String> {
	Iterable<UserPermission> findByOwnerId(String ownerId);

	Long deleteByOwnerId(String ownerId);

	Iterable<UserPermission> findByAuthorizedUsersId(String authorizedUserId);

	@Query("MATCH (:User {id:{ownerId}})-[:HAS_GRANTED]->(:UserPermission {scope:{scope}})-[:AUTHORIZES]->(authorizedUser:User {id:{authorizedUserId}}) " +
			"RETURN COUNT(authorizedUser) > 0")
	Boolean hasUserPermission(@Param("ownerId") String ownerId, @Param("authorizedUserId") String authorizedUserId,
							  @Param("scope") UserPermissionScope scope);

	@Query("MATCH (:User {id:{authorizedUserId}})<-[:AUTHORIZES]-(:UserPermission {scope:{scope}})<-[:HAS_GRANTED]-(user:User) " +
			"RETURN user")
	Iterable<User> findUsersWhoGrantedPermission(@Param("authorizedUserId") String authorizedUserId,
												 @Param("scope") UserPermissionScope scope);
}
