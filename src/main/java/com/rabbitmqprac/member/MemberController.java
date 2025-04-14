package com.rabbitmqprac.member;

import com.rabbitmqprac.member.dto.MemberCreateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getMember(@PathVariable Long memberId, Model model) {
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
    public String issueAccessToken(@RequestParam Long memberId) {
        return memberService.issueAccessToken(memberId);
    }
}
