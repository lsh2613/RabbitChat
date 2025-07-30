package com.rabbitmqprac.member;

import com.rabbitmqprac.member.dto.MemberDetailRes;
import com.rabbitmqprac.security.jwt.AccessTokenClaim;
import com.rabbitmqprac.security.jwt.RefreshTokenClaim;
import com.rabbitmqprac.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.util.AccessTokenProvider;
import com.rabbitmqprac.util.CookieUtil;
import com.rabbitmqprac.util.RefreshTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    // todo AuthController로 이동
    @GetMapping("/login/{memberId}")
    public ResponseEntity<?> login(@PathVariable("memberId") Long memberId) {
        Member member = memberService.readById(memberId);

        String accessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(member.getId(), member.getRole().getType()));
        String refreshToken = refreshTokenProvider.generateToken(RefreshTokenClaim.of(member.getId(), member.getRole().getType()));

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        ResponseCookie cookie = CookieUtil.createCookie("refreshToken", refreshToken, Duration.ofDays(7).toSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("memberId", memberId));
    }

    @GetMapping("/members/me")
    public MemberDetailRes getMember(@AuthenticationPrincipal SecurityUserDetails user) {
        return memberService.getMemberDetail(user.getUserId());
    }

    @GetMapping("/members")
    public List<Member> getMembers() {
        return memberService.getMembers();
    }
}
