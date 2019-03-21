package io.knowledgeassets.myskills.server.communityuser.query;

import io.knowledgeassets.myskills.server.community.CommunityRole;
import io.knowledgeassets.myskills.server.communityuser.CommunityUser;
import io.knowledgeassets.myskills.server.communityuser.CommunityUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
public class CommunityUserQueryService {

	private final CommunityUserRepository communityUserRepository;

	public CommunityUserQueryService(CommunityUserRepository communityUserRepository) {
		this.communityUserRepository = communityUserRepository;
	}

	@Transactional(readOnly = true)
	public Stream<CommunityUser> getCommunityUsers(String communityId, CommunityRole role) {
		if (communityId == null) {
			throw new IllegalArgumentException("Community ID cannot be null");
		}
		if (role == null) {
			return communityUserRepository.findByCommunityId(communityId);
		} else {
			return communityUserRepository.findByCommunityIdAndRole(communityId, role);
		}
	}

}
