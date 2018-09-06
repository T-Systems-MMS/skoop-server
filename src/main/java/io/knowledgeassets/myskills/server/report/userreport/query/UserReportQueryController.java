package io.knowledgeassets.myskills.server.report.userreport.query;

import io.knowledgeassets.myskills.server.report.skillpriorityreport.query.SkillPriorityReportQueryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Priority UserSkillPriorityReport", description = "API allowing queries of reports")
@RestController
public class UserReportQueryController {
	private SkillPriorityReportQueryService skillPriorityReportQueryService;

	public UserReportQueryController(SkillPriorityReportQueryService skillPriorityReportQueryService) {
		this.skillPriorityReportQueryService = skillPriorityReportQueryService;
	}

}
