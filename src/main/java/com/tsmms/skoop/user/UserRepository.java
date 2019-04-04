package com.tsmms.skoop.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
	Optional<User> findByUserName(String userName);

	Optional<User> findByReferenceId(String referenceId);

	@Query("MATCH (user:User) " +
			"WHERE TOLOWER(user.userName) CONTAINS TOLOWER({search}) " +
			"OR TOLOWER(user.firstName) CONTAINS TOLOWER({search}) " +
			"OR TOLOWER(user.lastName) CONTAINS TOLOWER({search}) " +
			"RETURN user ORDER BY user.userName LIMIT 20")
	Iterable<User> findBySearchTerm(@Param("search") String search);
}
