package io.knowledgeassets.myskills.server.community;

import java.util.stream.Stream;

public interface RecommendedCommunityRepository {

	Stream<Community> getRecommendedCommunities(String userId);

}
