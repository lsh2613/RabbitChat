package com.rabbitmqprac.infra.security.common.registry;

import com.rabbitmqprac.infra.stomp.exception.StompErrorCode;
import com.rabbitmqprac.infra.stomp.exception.StompErrorException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
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
public class ResourceAccessRegistry {
    private final Map<Pattern, ResourceAccessChecker> checkers = new HashMap<>();
    private final ChatRoomAccessChecker chatRoomAccessChecker;

    @PostConstruct
    public void setCheckers() {
        registerChecker("^/exchange/chat\\.exchange/room\\.\\d+$", chatRoomAccessChecker);
    }

    public void registerChecker(final String pathPattern, final ResourceAccessChecker checker) {
        checkers.put(Pattern.compile(pathPattern), checker);
    }

    /**
     * path에 대한 체커를 반환한다.
     *
     * @param path : 요청 경로
     * @return ResourceAccessChecker : path에 대한 체커
     * @throws IllegalArgumentException : 해당 경로에 대한 체커가 없는 경우
     */
    public Optional<ResourceAccessChecker> getChecker(final String path) {
        return checkers.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(path).matches())
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
