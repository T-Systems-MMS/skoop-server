package com.tsmms.skoop.scheduling;

import com.tsmms.skoop.user.command.UserCommandService;
import com.tsmms.skoop.user.query.UserQueryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

/**
 * The job to reconcile SKOOP users with the users got from Active Directory Federation Services.
 */
@Component
public class UserReconciliationJob {

	private final UserQueryService userQueryService;
	private final UserCommandService userCommandService;
	private final ActiveDirectoryServiceMock activeDirectoryServiceMock;

	public UserReconciliationJob(UserQueryService userQueryService,
								 UserCommandService userCommandService,
								 ActiveDirectoryServiceMock activeDirectoryServiceMock) {
		this.userQueryService = requireNonNull(userQueryService);
		this.userCommandService = requireNonNull(userCommandService);
		this.activeDirectoryServiceMock = requireNonNull(activeDirectoryServiceMock);
	}

	@Scheduled(cron = "${skoop.schedule.reconciliation.user:0 0 2 * * *}")
	public void run() {
		userQueryService.getUsers().forEach(user ->
				activeDirectoryServiceMock.getManagerByUser(user.getUserName()).ifPresent(manager -> {
					if (!manager.equals(user.getUserName())) {
						userCommandService.updateUserManager(user.getId(), manager);
					}
				})
		);
	}

}
