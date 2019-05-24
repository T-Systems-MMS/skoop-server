package com.tsmms.skoop.userproject.query;

import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserProjectQueryService {

	private final UserProjectRepository userProjectRepository;

	public UserProjectQueryService(UserProjectRepository userProjectRepository) {
		this.userProjectRepository = userProjectRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserProject> getUserProjects(String userId) {
		return StreamSupport.stream(userProjectRepository.findByUserId(userId)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	@PreAuthorize("isPrincipalUserId(#userId) or hasUserPermission(#userId, 'READ_USER_SKILLS')")
	public Optional<UserProject> getUserProjectByUserIdAndProjectId(String userId, String projectId) {
		return userProjectRepository.findByUserIdAndProjectId(userId, projectId);
	}

}
