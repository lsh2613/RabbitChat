package com.rabbitmqprac.security.authentication;

import com.rabbitmqprac.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonLocked;

    @Builder
    private SecurityUserDetails(Long userId, String username, Collection<? extends GrantedAuthority> authorities, boolean accountNonLocked) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
        this.accountNonLocked = accountNonLocked;
    }

    public static UserDetails from(User user) {
        return SecurityUserDetails.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .authorities(List.of(new CustomGrantedAuthority(user.getRole().getType())))
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "SecurityUserDetails{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", authorities=" + authorities +
                '}';
    }

}