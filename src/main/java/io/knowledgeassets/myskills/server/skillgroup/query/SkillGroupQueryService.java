package io.knowledgeassets.myskills.server.skillgroup.query;

import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SkillGroupQueryService {
	private SkillGroupRepository skillGroupRepository;

	public SkillGroupQueryService(SkillGroupRepository skillGroupRepository) {
		this.skillGroupRepository = skillGroupRepository;
	}

	@Transactional(readOnly = true)
	public Stream<SkillGroup> getSkillGroups() {
		return StreamSupport.stream(skillGroupRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<SkillGroup> getSkillGroupById(String skillGroupId) {
		return skillGroupRepository.findById(skillGroupId);
	}

	/**
	 * It ignores case sensitivity
	 *
	 * @param skillGroupName
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<SkillGroup> findByNameIgnoreCase(String skillGroupName) {
		return skillGroupRepository.findByNameIgnoreCase(skillGroupName);
	}

	@Transactional(readOnly = true)
	public boolean exists(String skillGroupId) throws EmptyInputException {
		if (skillGroupId == null) {
			throw EmptyInputException.builder()
					.message("skillGroupId is null.")
					.build();
		}
		return skillGroupRepository.existsById(skillGroupId);
	}

	public Boolean isSkillGroupExist(String search) {
		return skillGroupRepository.isSkillGroupExistByNameIgnoreCase(search);
	}

	@Transactional(readOnly = true)
	public Stream<SkillGroup> getSkillGroupSuggestions(String search) throws EmptyInputException, NoSuchResourceException {
		return StreamSupport.stream(skillGroupRepository.findSkillGroupSuggestions(search)
				.spliterator(), false);
	}
}
