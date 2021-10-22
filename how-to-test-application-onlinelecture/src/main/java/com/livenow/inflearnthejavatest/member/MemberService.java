package com.livenow.inflearnthejavatest.member;

import com.livenow.inflearnthejavatest.domain.Member;
import com.livenow.inflearnthejavatest.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

    void notify(Member member);
}
