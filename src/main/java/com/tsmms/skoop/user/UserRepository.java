package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
	Optional<User> findByUserName(String userName);

	@Query("MATCH (user:User {userName:{userName}}) RETURN user.id")
	Optional<String> findUserIdByUserName(@Param("userName") String userName);

	Optional<User> findByReferenceId(String referenceId);

	@Query("MATCH (user:User) " +
			"WHERE TOLOWER(user.userName) CONTAINS TOLOWER({search}) " +
			"OR TOLOWER(user.firstName) CONTAINS TOLOWER({search}) " +
			"OR TOLOWER(user.lastName) CONTAINS TOLOWER({search}) " +
			"RETURN user ORDER BY user.userName LIMIT 20")
	Iterable<User> findBySearchTerm(@Param("search") String search);

	Stream<User> findByManagerId(String managerId);
}
