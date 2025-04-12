package com.rabbitmqprac.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Member> createMember() {
        return ResponseEntity.ok(memberService.create());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    @GetMapping("/tokens")
    public ResponseEntity<String> issueAccessToken (@RequestParam Long memberId) {
        return ResponseEntity.ok(memberService.issueAccessToken(memberId));
    }
}
