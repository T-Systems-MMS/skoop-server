package io.knowledgeassets.myskills.server.notification;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface NotificationRepository extends Neo4jRepository<Notification, String> {

	/**
	 * Finds notifications addressed to a user or to communities she / he is manager of.
	 * @param userId - user ID
	 * @return notifications
	 */
	@Query("OPTIONAL MATCH (n:Notification)-[:CAUSED_BY]->(registration:CommunityUserRegistration)-[:registeredUser]->(registeredUser:User {id: {userId}}) " +
			" WITH collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserKickedOutFromCommunityNotification)-[:USER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityDeletedNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:RequestToJoinCommunityNotification)-[:CAUSED_BY]->(registration:CommunityUserRegistration)-[:community]->(c:Community)<-[:COMMUNITY_USER {role:'MANAGER'}]-(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserLeftCommunityNotification)-[:COMMUNITY]->(c:Community)<-[:COMMUNITY_USER {role:'MANAGER'}]-(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" UNWIND notifications as n " +
			" OPTIONAL MATCH (n)-[r1:CAUSED_BY]->(registration:CommunityUserRegistration)-[r2:registeredUser]->(registeredUser:User) " +
			" WITH n, r1, registration, r2, registeredUser " +
			" OPTIONAL MATCH (registration)-[r3:community]->(c:Community) " +
			" WITH n, r1, registration, r2, registeredUser, r3, c " +
			" OPTIONAL MATCH (user:User)<-[r4:USER]-(n)-[r5:COMMUNITY]->(community:Community) " +
			" RETURN n, r1, registration, r2, registeredUser, r3, c, user, r4, r5, community " +
			" ORDER BY n.creationDatetime DESC")
	Stream<Notification> getUserNotifications(@Param("userId") String userId);

}
