package io.knowledgeassets.myskills.server.report.userskillpriority;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.*;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

/**
 * Query result representing an aggregation of priorities for the relationships between users and a particular skill.
 * Includes both the skill entity and the list of related user entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class UserSkillPriorityAggregationReportResult {
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
	private Skill skill;
	@EqualsAndHashCode.Exclude
	private List<User> users;
}
