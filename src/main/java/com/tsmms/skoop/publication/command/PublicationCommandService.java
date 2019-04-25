package com.tsmms.skoop.publication.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.publication.PublicationRepository;
import com.tsmms.skoop.skill.command.SkillCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static com.tsmms.skoop.exception.enums.Model.PUBLICATION;

@Service
public class PublicationCommandService {

	private final PublicationRepository publicationRepository;
	private final SkillCommandService skillCommandService;

	public PublicationCommandService(PublicationRepository publicationRepository,
									 SkillCommandService skillCommandService) {
		this.publicationRepository = requireNonNull(publicationRepository);
		this.skillCommandService = requireNonNull(skillCommandService);
	}

	@Transactional
	public Publication create(Publication publication) {
		final LocalDateTime now = LocalDateTime.now();
		publication.setId(UUID.randomUUID().toString());
		publication.setCreationDatetime(now);
		publication.setLastModifiedDatetime(now);
		publication.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(publication.getSkills())));
		return publicationRepository.save(publication);
	}

	@Transactional
	public Publication update(String publicationId, PublicationUpdateCommand command) {
		if (publicationId == null) {
			throw new IllegalArgumentException("Publication ID cannot be null.");
		}
		if (command == null) {
			throw new IllegalArgumentException("Command to update publication cannot be null.");
		}
		final Publication publication = publicationRepository.findById(publicationId).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", publicationId};
			return NoSuchResourceException.builder()
					.model(PUBLICATION)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		publication.setLastModifiedDatetime(LocalDateTime.now());
		publication.setLink(command.getLink());
		publication.setPublisher(command.getPublisher());
		publication.setDate(command.getDate());
		publication.setTitle(command.getTitle());
		publication.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(command.getSkills())));
		return publicationRepository.save(publication);
	}

	@Transactional
	public void delete(String publicationId) {
		if (publicationId == null) {
			throw new IllegalArgumentException("Publication ID cannot be null.");
		}
		if (publicationRepository.findById(publicationId).isEmpty()) {
			final String[] searchParamsMap = {"id", publicationId};
			throw NoSuchResourceException.builder()
					.model(PUBLICATION)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		publicationRepository.deleteById(publicationId);
	}

}
