package com.tsmms.skoop.search.query;

import com.tsmms.skoop.search.AnonymousUserSkillRepository;
import com.tsmms.skoop.search.AnonymousUserSkillResult;
import com.tsmms.skoop.search.UserSearchSkillCriterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class AnonymousUserSkillSearchService {

	private final AnonymousUserSkillRepository anonymousUserSkillRepository;

	public AnonymousUserSkillSearchService(AnonymousUserSkillRepository anonymousUserSkillRepository) {
		this.anonymousUserSkillRepository = requireNonNull(anonymousUserSkillRepository);
	}

	@Transactional(readOnly = true)
	public Stream<AnonymousUserSkillResult> findBySkillLevel(List<UserSearchSkillCriterion> searchParams) {
		if (searchParams == null || searchParams.isEmpty()) {
			throw new IllegalArgumentException("There are no parameters to search by.");
		}
		return anonymousUserSkillRepository.findAnonymousUserSkillsBySkillLevels(searchParams);
	}

}
