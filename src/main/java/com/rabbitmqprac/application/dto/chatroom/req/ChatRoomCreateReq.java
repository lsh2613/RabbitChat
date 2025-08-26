package com.rabbitmqprac.application.dto.chatroom.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "chatRoomCreateReq", title = "채팅방 생성 요청 DTO")
public record ChatRoomCreateReq(
        @Schema(description = "채팅방 제목", example = "스프링 스터디")
        @NotBlank
        @Size(min = 1, max = 20)
        String title,
        @Schema(description = "채팅방 최대 인원", example = "5")
        @NotNull
        @Size(min = 2, max = 10)
        Integer maxCapacity
) {

}
