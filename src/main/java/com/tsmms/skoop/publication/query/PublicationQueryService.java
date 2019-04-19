package com.tsmms.skoop.publication.query;

import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.publication.PublicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class PublicationQueryService {

	private final PublicationRepository publicationRepository;

	public PublicationQueryService(PublicationRepository publicationRepository) {
		this.publicationRepository = requireNonNull(publicationRepository);
	}

	@Transactional(readOnly = true)
	public Stream<Publication> getUserPublications(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		return publicationRepository.findByUserIdOrderByDateDesc(userId);
	}

}
