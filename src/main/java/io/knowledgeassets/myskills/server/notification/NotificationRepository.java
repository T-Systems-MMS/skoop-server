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
	@Query("MATCH (n:Notification)-[:USER_RECIPIENT]->(:User {id: {userId}}) " +
			" WITH collect(n) AS notifications " +
			" MATCH (n:Notification)-[:COMMUNITY_RECIPIENT]->(c:Community)<-[:COMMUNITY_USER {role:'MANAGER'}]-(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" UNWIND notifications as n " +
			" OPTIONAL MATCH (n)-[r1:ATTACHED_TO]->(registration:CommunityUserRegistration)-[r4:registeredUser]-(registeredUser:User) " +
			" WITH n, r1, registration, registeredUser, r4 " +
			" OPTIONAL MATCH (n)-[r2:USER_RECIPIENT]->(user:User) " +
			" WITH n, r1, registration, r2, user, registeredUser, r4 " +
			" OPTIONAL MATCH (n)-[r3:COMMUNITY_RECIPIENT]->(community:Community) " +
			" WITH n, r1, registration, r2, user, r3, community, registeredUser, r4 " +
			" OPTIONAL MATCH (registration)-[r5:community]->(c:Community) " +
			" RETURN n, r1, registration, r2, user, r3, community, registeredUser, r4, c, r5 " +
			" ORDER BY n.creationDatetime DESC")
	Stream<Notification> getUserNotifications(@Param("userId") String userId);

}
