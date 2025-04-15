package com.rabbitmqprac.member;

import com.rabbitmqprac.common.annotation.Requester;
import com.rabbitmqprac.member.dto.MemberCreateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public String createMember(Model model) {
        MemberCreateRes memberCreateRes = memberService.create();
        model.addAttribute("memberCreateRes", memberCreateRes);
        return "member-create";
    }

    @GetMapping("/{memberId}")
    public String getMember(@Requester Long memberId, Model model) {
        Member member = memberService.getMember(memberId);
        model.addAttribute("member", member);
        return "member";
    }

    @GetMapping
    public String getMembers(Model model) {
        List<Member> members = memberService.getMembers();
        model.addAttribute("members", members);
        return "members";
    }

    @ResponseBody
    @GetMapping("/tokens")
    public String issueAccessToken(@Requester Long memberId) {
        return memberService.issueAccessToken(memberId);
    }
}
