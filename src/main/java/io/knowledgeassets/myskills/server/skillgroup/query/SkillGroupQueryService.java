package io.knowledgeassets.myskills.server.skillgroup.query;

import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
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
	private SkillQueryService skillQueryService;

	public SkillGroupQueryService(SkillGroupRepository skillGroupRepository, SkillQueryService skillQueryService) {
		this.skillGroupRepository = skillGroupRepository;
		this.skillQueryService = skillQueryService;
	}

	@Transactional(readOnly = true)
	public Stream<SkillGroup> getGroups() {
		return StreamSupport.stream(skillGroupRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<SkillGroup> getGroupById(String skillGroupId) {
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

	public Boolean isGroupExist(String search) {
		return skillGroupRepository.isGroupExistByNameIgnoreCase(search);
	}

	@Transactional(readOnly = true)
	public Stream<SkillGroup> getSkillGroupSuggestionsBySkillId(String skillId, String search) throws EmptyInputException, NoSuchResourceException {
		if (!skillQueryService.exists(skillId)) {
			String[] searchParamsMap = {"id", skillId};
			throw NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(skillGroupRepository.findGroupSuggestionsBySkillId(skillId, search)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<SkillGroup> getSkillGroupSuggestions(String search) throws EmptyInputException, NoSuchResourceException {
		return StreamSupport.stream(skillGroupRepository.findGroupSuggestions(search)
				.spliterator(), false);
	}
}
