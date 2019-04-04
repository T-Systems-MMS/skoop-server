package com.tsmms.skoop.userskill;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSkillRepository extends Neo4jRepository<UserSkill, Long> {
	Iterable<UserSkill> findByUserId(String userId);

	Iterable<UserSkill> findByUserIdOrderByCurrentLevelDesc(String userId);

	Iterable<UserSkill> findBySkillId(String skillId);

	Iterable<UserSkill> findBySkillIdAndPriorityGreaterThanEqual(String skillId, Integer minPriority);

	Optional<UserSkill> findByUserIdAndSkillId(String userId, String skillId);

	@Query("MATCH (user:User {id:{userId}})-[userSkill:RELATED_TO]-(:Skill {id:{skillId}})" +
			"-[coachSkill:RELATED_TO]-(coach:User) " +
			"WHERE coachSkill.currentLevel >= userSkill.desiredLevel AND coach.coach = true " +
			"RETURN coach " +
			"LIMIT 20")
	Iterable<User> findCoachesByUserIdAndSkillId(@Param("userId") String userId, @Param("skillId") String skillId);

	@Query("MATCH (skill:Skill)-[userSkill:RELATED_TO]-(:User) " +
			"WHERE userSkill.priority > 0 " +
			"RETURN skill AS skill, AVG(userSkill.priority) AS averagePriority, " +
			"MAX(userSkill.priority) AS maximumPriority, COUNT(*) AS userCount " +
			"ORDER BY AVG(userSkill.priority) DESC, COUNT(*) DESC, MAX(userSkill.priority) DESC " +
			"LIMIT 10")
	Iterable<UserSkillPriorityAggregationResult> findTop10PrioritizedSkills();

	@Query("MATCH (skill:Skill) " +
			"WHERE NOT EXISTS((skill)-[:RELATED_TO]-(:User {id:{userId}})) " +
			"AND TOLOWER(skill.name) CONTAINS TOLOWER({search}) " +
			"RETURN skill " +
			"ORDER BY skill.name ASC " +
			"LIMIT 20")
	Iterable<Skill> findSkillSuggestionsByUserId(@Param("userId") String userId, @Param("search") String search);
}
