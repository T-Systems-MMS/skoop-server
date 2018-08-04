package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
public class SkillCommandService {
	private CommandGateway commandGateway;
	private SkillQueryRepository skillQueryRepository;

	public SkillCommandService(CommandGateway commandGateway, SkillQueryRepository skillQueryRepository) {
		this.commandGateway = commandGateway;
		this.skillQueryRepository = skillQueryRepository;
	}

	@Transactional
	public Skill createSkill(String name, String description) {
		String id = commandGateway.sendAndWait(new CreateSkillCommand(name, description));
		return skillQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
				format("Skill with ID '%s' not found", id)));
	}

	@Transactional
	public Skill updateSkill(String id, String name, String description) {
		commandGateway.sendAndWait(new UpdateSkillCommand(id, name, description));
		return skillQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
				format("Skill with ID '%s' not found", id)));
	}

	@Transactional
	public void deleteSkill(String skillId) {
		commandGateway.sendAndWait(new DeleteSkillCommand(skillId));
	}
}
