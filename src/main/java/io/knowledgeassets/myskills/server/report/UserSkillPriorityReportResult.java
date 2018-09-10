package io.knowledgeassets.myskills.server.report;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import lombok.*;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class UserSkillPriorityReportResult {

	private Double averagePriority;
	private Double maximumPriority;
	private Integer userCount;

	@EqualsAndHashCode.Exclude
	private List<User> users;
}
