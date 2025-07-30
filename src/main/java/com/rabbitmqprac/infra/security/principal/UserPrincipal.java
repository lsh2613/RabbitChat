package com.rabbitmqprac.infra.security.principal;

import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.security.Principal;
import java.time.LocalDateTime;

@Getter
public class UserPrincipal implements Principal {
    private final Long userId;
    private String username;
    private Role role;
    private LocalDateTime expiresAt;

    @Builder
    private UserPrincipal(Long userId, String username, Role role, LocalDateTime expiresAt) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.expiresAt = expiresAt;
    }

    public static UserPrincipal from(User user, LocalDateTime expiresAt) {
        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
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
        int result = userId.hashCode() * 31;
        return result + username.hashCode() * 31;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserPrincipal that = (UserPrincipal) obj;
        return userId.equals(that.userId);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

