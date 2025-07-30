package com.rabbitmqprac.security.principal;

import com.rabbitmqprac.member.Member;
import com.rabbitmqprac.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.security.Principal;
import java.time.LocalDateTime;

@Getter
public class MemberPrincipal implements Principal {
    private final Long memberId;
    private String nickname;
    private Role role;
    private LocalDateTime expiresAt;

    @Builder
    private MemberPrincipal(Long memberId, String nickname, Role role, LocalDateTime expiresAt) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.role = role;
        this.expiresAt = expiresAt;
    }

    public static MemberPrincipal from(Member member, LocalDateTime expiresAt) {
        return MemberPrincipal.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .role(member.getRole())
                .expiresAt(expiresAt)
                .build();
    }

    public void updateExpiresAt(LocalDateTime expiresAt) {
        if (expiresAt.isBefore(this.expiresAt)) {
            throw new IllegalArgumentException("만료 시간을 줄일 수 없습니다.");
        }

        this.expiresAt = expiresAt;
    }

    @Override
    public int hashCode() {
        int result = memberId.hashCode() * 31;
        return result + nickname.hashCode() * 31;
    }

    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MemberPrincipal that = (MemberPrincipal) obj;
        return memberId.equals(that.memberId);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "userId=" + memberId +
                ", username='" + nickname + '\'' +
                ", role=" + role +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

