package io.knowledgeassets.myskills.server.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.annotation.QueryResult;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@QueryResult
public class RecommendedCommunity {

	private Community community;
	private boolean recommended;
	private Long skillCounter;

}
