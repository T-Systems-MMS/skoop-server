package com.tsmms.skoop.communityuser.registration.query;

import com.tsmms.skoop.communityuser.registration.CommunityUserRegistration;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CommunityUserRegistrationQueryService {

	private final CommunityUserRegistrationRepository communityUserRegistrationRepository;

	public CommunityUserRegistrationQueryService(CommunityUserRegistrationRepository communityUserRegistrationRepository) {
		this.communityUserRegistrationRepository = communityUserRegistrationRepository;
	}

	@Transactional(readOnly = true)
	public Optional<CommunityUserRegistration> getCommunityUserRegistrationById(String registrationId) {
		return communityUserRegistrationRepository.findById(registrationId);
	}

	@Transactional(readOnly = true)
	public Optional<CommunityUserRegistration> getPendingUserRequestToJoinCommunity(String userId, String communityId) {
		return communityUserRegistrationRepository
				.findByRegisteredUserIdAndCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull(userId, communityId);
	}

	@Transactional(readOnly = true)
	public Stream<CommunityUserRegistration> getPendingUserRequestsToJoinCommunity(String communityId) {
		if (communityId == null) {
			throw new IllegalArgumentException("Community ID cannot be null.");
		}
		return communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsTrueAndApprovedByCommunityIsNull(communityId);
	}

	@Transactional(readOnly = true)
	public Stream<CommunityUserRegistration> getPendingInvitationsToJoinCommunity(String communityId) {
		if (communityId == null) {
			throw new IllegalArgumentException("Community ID cannot be null.");
		}
		return communityUserRegistrationRepository.findByCommunityIdAndApprovedByUserIsNullAndApprovedByCommunityIsTrue(communityId);
	}

}
