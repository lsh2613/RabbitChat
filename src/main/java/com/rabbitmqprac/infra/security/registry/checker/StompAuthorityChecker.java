package com.rabbitmqprac.infra.security.registry.checker;

import java.security.Principal;

/**
 * 리소스 접근 권한을 확인하는 인터페이스
 */
public interface StompAuthorityChecker {
    /**
     * 리소스에 대한 접근 권한을 확인한다.
     *
     * @param id      : 요청 리소스 ID
     * @param principal : 요청자
     * @return 접근 권한 여부
     */
    boolean hasPermission(Long id, Principal principal);
}
