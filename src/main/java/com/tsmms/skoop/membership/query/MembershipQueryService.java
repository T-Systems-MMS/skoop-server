package com.tsmms.skoop.membership.query;

import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.membership.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class MembershipQueryService {

	private final MembershipRepository membershipRepository;

	public MembershipQueryService(MembershipRepository membershipRepository) {
		this.membershipRepository = requireNonNull(membershipRepository);
	}

	public Stream<Membership> getUserMemberships(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		return membershipRepository.findByUserIdOrderByDateDesc(userId);
	}

}
