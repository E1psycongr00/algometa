package com.lhgpds.algometa.internal.member.repository;

import com.lhgpds.algometa.internal.member.domain.entity.Member;
import com.lhgpds.algometa.internal.member.domain.vo.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    Optional<Member> findByEmail(Email email);

}