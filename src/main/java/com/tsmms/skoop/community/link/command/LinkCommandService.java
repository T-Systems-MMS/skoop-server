package com.tsmms.skoop.community.link.command;

import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.community.link.LinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Service
public class LinkCommandService {

	private final LinkRepository linkRepository;

	public LinkCommandService(LinkRepository linkRepository) {
		this.linkRepository = requireNonNull(linkRepository);
	}

	@Transactional
	public void delete(Link link) {
		linkRepository.delete(link);
	}

	@Transactional
	public void delete(Iterable<? extends Link> links) {
		linkRepository.deleteAll(links);
	}
}
