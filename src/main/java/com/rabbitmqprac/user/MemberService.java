package com.rabbitmqprac.user;


import com.rabbitmqprac.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenUtil tokenUtil;

    @Transactional
    public Member create() {
        return memberRepository.save(Member.create());
    }

    @Transactional(readOnly = true)
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    public String issueAccessToken(Long memberId) {
        return tokenUtil.issueAccessToken(memberId);
    }
}
