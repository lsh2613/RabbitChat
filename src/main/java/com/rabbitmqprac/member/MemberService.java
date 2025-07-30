package com.rabbitmqprac.member;


import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.member.dto.MemberDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member readById(Long memberId) {
        return memberRepository.findById(memberId).
                orElseThrow(() -> new GlobalErrorException(MemberErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Member> readUser(long memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberDetailRes getMemberDetail(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GlobalErrorException(MemberErrorCode.NOT_FOUND));
        return MemberMapper.toDetailRes(member);
    }
}
