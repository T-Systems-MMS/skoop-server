package com.tsmms.skoop.search;

import java.util.List;
import java.util.stream.Stream;

public interface AnonymousUserSkillRepository {

	Stream<AnonymousUserSkillResult> findAnonymousUserSkillsBySkillLevels(List<UserSearchSkillCriterion> searchParams);

}
