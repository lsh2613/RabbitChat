package com.rabbitmqprac.member;

import com.rabbitmqprac.global.annotation.Requester;
import com.rabbitmqprac.member.dto.MemberCreateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public MemberCreateRes createMember() {
        return memberService.create();
    }

    @GetMapping("/{memberId}")
    public Member getMember(@Requester Long memberId) {
        return memberService.getMember(memberId);
    }

    @GetMapping
    public List<Member> getMembers() {
        return memberService.getMembers();
    }

    @GetMapping("/tokens")
    public String issueAccessToken(@RequestParam Long memberId) {
        return memberService.issueAccessToken(memberId);
    }
}
