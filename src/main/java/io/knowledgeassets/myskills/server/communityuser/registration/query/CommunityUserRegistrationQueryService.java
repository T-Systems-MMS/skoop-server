package io.knowledgeassets.myskills.server.communityuser.registration.query;

import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

}
