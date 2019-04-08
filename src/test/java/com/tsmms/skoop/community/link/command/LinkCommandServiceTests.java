package com.tsmms.skoop.community.link.command;

import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.community.link.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class LinkCommandServiceTests {

	@Mock
	private LinkRepository linkRepository;

	private LinkCommandService linkCommandService;

	@BeforeEach
	void setUp() {
		linkCommandService = new LinkCommandService(linkRepository);
	}

	@DisplayName("Deletes a link.")
	@Test
	void deleteLink() {
		assertDoesNotThrow(() -> linkCommandService.delete(Link.builder()
				.id(12L)
				.name("Linkedin")
				.href("https://linkedin.com")
				.build()
		));
	}

}
