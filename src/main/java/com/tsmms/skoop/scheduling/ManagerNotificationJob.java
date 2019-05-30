package com.tsmms.skoop.scheduling;

import com.google.common.collect.Sets;
import com.tsmms.skoop.email.ManagerNotificationService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.query.UserProjectQueryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

@Component
public class ManagerNotificationJob {

	private final ManagerNotificationService managerNotificationService;
	private final UserProjectQueryService userProjectQueryService;

	public ManagerNotificationJob(ManagerNotificationService managerNotificationService,
								  UserProjectQueryService userProjectQueryService) {
		this.managerNotificationService = requireNonNull(managerNotificationService);
		this.userProjectQueryService = requireNonNull(userProjectQueryService);
	}

	@Scheduled(cron = "${skoop.schedule.notification.manager-pending-approval:0 0 3 * * *}")
	public void sendNotifications() {
		final Map<String, User> managerIdMap = new HashMap<>();
		final Map<String, Set<UserProject>> userProjects = userProjectQueryService.getNotApprovedUserProjects()
				.filter(up -> {
					final boolean managerIsPresent = up.getUser().getManager() != null;
					if (managerIsPresent) {
						managerIdMap.putIfAbsent(up.getUser().getManager().getId(), up.getUser().getManager());
					}
					return managerIsPresent;
				})
				.collect(toMap(up -> up.getUser().getManager().getId(), Collections::singleton, Sets::union));
		if (userProjects.isEmpty()) {
			return;
		}
		userProjects.forEach((userId, userProjectSet) -> managerNotificationService.send(managerIdMap.get(userId), userProjectSet));
	}

}
