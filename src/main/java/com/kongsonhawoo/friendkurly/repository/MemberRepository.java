package com.kongsonhawoo.friendkurly.repository;

import com.kongsonhawoo.friendkurly.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
