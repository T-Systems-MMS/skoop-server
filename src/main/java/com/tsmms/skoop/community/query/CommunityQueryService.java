package com.tsmms.skoop.community.query;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CommunityQueryService {

	private final CommunityRepository communityRepository;

	public CommunityQueryService(CommunityRepository communityRepository) {
		this.communityRepository = communityRepository;
	}

	@Transactional(readOnly = true)
	public Stream<Community> getCommunities() {
		return StreamSupport.stream(communityRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<Community> getCommunitiesRecommendedForUser(String userId) {
		return communityRepository.getRecommendedCommunities(userId);
	}

	@Transactional(readOnly = true)
	public Optional<Community> getCommunityById(String communityId) {
		return communityRepository.findById(communityId);
	}

	@Transactional(readOnly = true)
	public boolean hasCommunityManagerRole(String userId, String communityId) {
		return communityRepository.isCommunityManager(userId, communityId);
	}

	@Transactional(readOnly = true)
	public boolean isCommunityMember(String userId, String communityId) {
		return communityRepository.isCommunityMember(userId, communityId);
	}

	@Transactional(readOnly = true)
	public Stream<Community> getUserCommunities(String userId) {
		return communityRepository.getUserCommunities(userId);
	}

}
