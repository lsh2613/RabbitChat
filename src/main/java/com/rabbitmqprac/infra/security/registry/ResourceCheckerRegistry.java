package com.rabbitmqprac.infra.security.registry;

import com.rabbitmqprac.infra.security.registry.checker.ChatRoomAccessChecker;
import com.rabbitmqprac.infra.security.registry.checker.StompAuthorityChecker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 리소스 접근 권한 체커를 관리하는 레지스트리
 * path에 대한 checker를 내부적으로 관리한다.
 */
@RequiredArgsConstructor
@Component
public class ResourceCheckerRegistry {
    private final Map<Pattern, StompAuthorityChecker> checkers = new HashMap<>();
    private final ChatRoomAccessChecker chatRoomAccessChecker;

    @PostConstruct
    public void setCheckers() {
        registerChecker("^/exchange/chat\\.exchange/room\\.\\d+$", chatRoomAccessChecker);
    }

    public void registerChecker(final String pathPattern, final StompAuthorityChecker checker) {
        checkers.put(Pattern.compile(pathPattern), checker);
    }

    /**
     * path에 대한 체커를 반환한다.
     *
     * @param path : 요청 경로
     * @return ResourceAccessChecker : path에 대한 체커
     * @throws IllegalArgumentException : 해당 경로에 대한 체커가 없는 경우
     */
    public Optional<StompAuthorityChecker> getChecker(final String path) {
        return checkers.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(path).matches())
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
