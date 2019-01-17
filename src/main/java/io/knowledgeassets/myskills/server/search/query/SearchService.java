package io.knowledgeassets.myskills.server.search.query;

import io.knowledgeassets.myskills.server.search.AnonymousUserSkillRepository;
import io.knowledgeassets.myskills.server.search.AnonymousUserSkillResult;
import io.knowledgeassets.myskills.server.search.UserSearchSkillCriterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class SearchService {

	private final AnonymousUserSkillRepository anonymousUserSkillRepository;

	public SearchService(AnonymousUserSkillRepository anonymousUserSkillRepository) {
		this.anonymousUserSkillRepository = requireNonNull(anonymousUserSkillRepository);
	}

	@Transactional(readOnly = true)
	public Stream<AnonymousUserSkillResult> findAnonymousUserSkillsBySkillLevel(List<UserSearchSkillCriterion> searchParams) {
		if (searchParams == null || searchParams.isEmpty()) {
			throw new IllegalArgumentException("There are no parameters to search by.");
		}
		return anonymousUserSkillRepository.findAnonymousUserSkillsBySkillLevels(searchParams);
	}

}
