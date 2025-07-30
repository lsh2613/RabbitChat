package com.rabbitmqprac.domain.context.auth.service;

import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userService.readUser(Long.parseLong(userId));
        return SecurityUserDetails.from(user);
    }
}
