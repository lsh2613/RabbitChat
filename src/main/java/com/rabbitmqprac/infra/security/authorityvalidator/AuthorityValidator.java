package com.rabbitmqprac.infra.security.authorityvalidator;

import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.context.user.exception.UserErrorException;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import org.springframework.security.core.Authentication;

public abstract class AuthorityValidator {
    protected SecurityUserDetails isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserErrorException(UserErrorCode.FORBIDDEN);
        }

        return (SecurityUserDetails) authentication.getPrincipal();
    }
}
