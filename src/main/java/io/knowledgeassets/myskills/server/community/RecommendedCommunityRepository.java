package io.knowledgeassets.myskills.server.community;

import java.util.stream.Stream;

public interface RecommendedCommunityRepository {

	Stream<RecommendedCommunity> getRecommendedCommunities(String userId);

}
