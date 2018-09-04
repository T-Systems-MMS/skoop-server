package io.knowledgeassets.myskills.server.report;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.*;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

/**
 * The result from {@link io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService}
 * .findPrioritizedSkillsToCreateReport() method, is a list of objects from this class.
 * We later save this list of objects, as a report in {@link io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport} entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class UserSkillPriorityAggregationReport {
	private Skill skill;
	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;
	@EqualsAndHashCode.Exclude
	private List<User> users;
}
