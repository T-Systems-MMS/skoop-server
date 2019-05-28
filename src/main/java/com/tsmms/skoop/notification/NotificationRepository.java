package com.tsmms.skoop.notification;

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
			" OPTIONAL MATCH (n:CommunityUserRoleChangedNotification)-[:USER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityDeletedNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityChangedNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserSkillsEstimationNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserProjectNeedsApprovalNotification)-[:USER_PROJECT]->(:UserProject)-[:USER]->(:User)-[:MANAGER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserWelcomeNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
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
			" OPTIONAL MATCH (n)-[r4:COMMUNITY]->(community:Community) " +
			" WITH n, r1, registration, r2, registeredUser, r3, c, r4, community " +
			" OPTIONAL MATCH (n)-[r5:USER]->(user:User) " +
			" WITH n, r1, registration, r2, registeredUser, r3, c, r4, community, r5, user " +
			" OPTIONAL MATCH (n)-[r6:SKILL]->(skill:Skill) " +
			" WITH n, r1, registration, r2, registeredUser, r3, c, r4, community, r5, user, r6, skill " +
			" OPTIONAL MATCH (n)-[r7:USER_PROJECT]->(up:UserProject)-[r8:USER]->(u:User) " +
			" RETURN n, r1, registration, r2, registeredUser, r3, c, r4, community, r5, user, r6, skill, r7, up, r8, u" +
			" ORDER BY n.creationDatetime DESC")
	Stream<Notification> getUserNotifications(@Param("userId") String userId);

	/**
	 * Counts number of user notifications.
	 * @param userId - user ID
	 * @return number of user notifications
	 */
	@Query("OPTIONAL MATCH (n:Notification)-[:CAUSED_BY]->(registration:CommunityUserRegistration)-[:registeredUser]->(registeredUser:User {id: {userId}}) " +
			" WITH collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserKickedOutFromCommunityNotification)-[:USER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityUserRoleChangedNotification)-[:USER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityDeletedNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:CommunityChangedNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserSkillsEstimationNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserProjectNeedsApprovalNotification)-[:USER_PROJECT]->(:UserProject)-[:USER]->(:User)-[:MANAGER]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserWelcomeNotification)-[:RECIPIENT]->(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:RequestToJoinCommunityNotification)-[:CAUSED_BY]->(registration:CommunityUserRegistration)-[:community]->(c:Community)<-[:COMMUNITY_USER {role:'MANAGER'}]-(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" OPTIONAL MATCH (n:UserLeftCommunityNotification)-[:COMMUNITY]->(c:Community)<-[:COMMUNITY_USER {role:'MANAGER'}]-(:User {id: {userId}}) " +
			" WITH notifications + collect(n) AS notifications " +
			" UNWIND notifications as n " +
			" RETURN count(n)")
	int getUserNotificationCounter(@Param("userId") String userId);

	@Query("MATCH (n:Notification)-[:CAUSED_BY]->(registration:CommunityUserRegistration {id: {registrationId}}) RETURN n")
	Stream<Notification> findByRegistrationId(@Param("registrationId") String registrationId);

}
