package com.rabbitmqprac.application.dto.chatroom.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatRoomCreateReq(
        @NotBlank
        @Size(min = 1, max = 20)
        String title,
        @NotNull
        @Size(min = 2, max = 10)
        Integer maxCapacity
) {

}
