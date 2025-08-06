package com.rabbitmqprac.domain.persistence.usersession.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE_APP("앱 활성화"),
    ACTIVE_CHAT_ROOM_LIST("채팅방 리스트 뷰"),
    ACTIVE_CHAT_ROOM("채팅방 뷰"),
    BACKGROUND("백그라운드"),
    INACTIVE("비활성화"),
    ;

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
