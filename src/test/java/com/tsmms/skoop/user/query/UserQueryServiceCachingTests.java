package com.tsmms.skoop.user.query;

import com.tsmms.skoop.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {"liquigraph.enabled=false"})
@ActiveProfiles("test")
class UserQueryServiceCachingTests {
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private ConversionService conversionService;
	@MockBean
	private PlatformTransactionManager transactionManager;

	@Autowired
	private UserQueryService userQueryService;

	@Test
	@DisplayName("Retrieves the user ID from the repository only once if user exists")
	void retrievesUserIdFromRepositoryOnlyOnceForExistingUser() {
		given(userRepository.findUserIdByUserName("johndoe")).willReturn(Optional.of("123"));
		userQueryService.getUserIdByUserName("johndoe");
		userQueryService.getUserIdByUserName("johndoe");
		userQueryService.getUserIdByUserName("johndoe");
		then(userRepository).should(times(1)).findUserIdByUserName("johndoe");
		then(userRepository).shouldHaveNoMoreInteractions();
	}

	@Test
	@DisplayName("Retrieves the user ID from the repository again if user does not exist")
	void retrievesUserIdFromRepositoryAgainForNonExistingUser() {
		given(userRepository.findUserIdByUserName("johndoe")).willReturn(Optional.empty());
		userQueryService.getUserIdByUserName("johndoe");
		userQueryService.getUserIdByUserName("johndoe");
		userQueryService.getUserIdByUserName("johndoe");
		then(userRepository).should(times(3)).findUserIdByUserName("johndoe");
		then(userRepository).shouldHaveNoMoreInteractions();
	}
}
