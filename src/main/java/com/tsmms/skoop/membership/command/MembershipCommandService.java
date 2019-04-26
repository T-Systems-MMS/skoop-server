package com.tsmms.skoop.membership.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.membership.MembershipRepository;
import com.tsmms.skoop.skill.command.SkillCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static com.tsmms.skoop.exception.enums.Model.MEMBERSHIP;
import static java.util.Objects.requireNonNull;

@Service
public class MembershipCommandService {

	private final SkillCommandService skillCommandService;
	private final MembershipRepository membershipRepository;

	public MembershipCommandService(SkillCommandService skillCommandService, MembershipRepository membershipRepository) {
		this.skillCommandService = requireNonNull(skillCommandService);
		this.membershipRepository = requireNonNull(membershipRepository);
	}

	@Transactional
	public Membership create(Membership membership) {
		final LocalDateTime now = LocalDateTime.now();
		membership.setId(UUID.randomUUID().toString());
		membership.setCreationDatetime(now);
		membership.setLastModifiedDatetime(now);
		membership.setSkills(skillCommandService.createNonExistentSkills(membership.getSkills()));
		return membershipRepository.save(membership);
	}

	@Transactional
	public Membership update(String membershipId, MembershipUpdateCommand command) {
		if (membershipId == null) {
			throw new IllegalArgumentException("Membership ID cannot be null.");
		}
		if (command == null) {
			throw new IllegalArgumentException("Command to update membership cannot be null.");
		}
		final Membership membership = membershipRepository.findById(membershipId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", membershipId};
			return NoSuchResourceException.builder()
					.model(MEMBERSHIP)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		membership.setLastModifiedDatetime(LocalDateTime.now());
		membership.setLink(command.getLink());
		membership.setDescription(command.getDescription());
		membership.setName(command.getName());
		membership.setSkills(skillCommandService.createNonExistentSkills(command.getSkills()));
		return membershipRepository.save(membership);
	}

	@Transactional
	public void delete(String membershipId) {
		if (membershipId == null) {
			throw new IllegalArgumentException("Membership ID cannot be null.");
		}
		if (membershipRepository.findById(membershipId).isEmpty()) {
			final String[] searchParamsMap = {"id", membershipId};
			throw NoSuchResourceException.builder()
					.model(MEMBERSHIP)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		membershipRepository.deleteById(membershipId);
	}

}
