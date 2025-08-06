package com.rabbitmqprac.infra.batch.writer;

import com.rabbitmqprac.domain.persistence.chatmessagestatus.entity.ChatMessageStatus;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.repository.ChatMessageStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LastMessageIdWriter implements ItemWriter<ChatMessageStatus> {
    private final ChatMessageStatusRepository repository;

    @Override
    public void write(Chunk<? extends ChatMessageStatus> chunk) throws Exception {
        log.debug("Writing chunk size: {}", chunk.getItems().size());

        Map<Long, Map<Long, Long>> updates = chunk.getItems().stream()
                .collect(
                        Collectors.groupingBy(
                                ChatMessageStatus::getUserId,
                                Collectors.toMap(
                                        ChatMessageStatus::getChatRoomId,
                                        ChatMessageStatus::getLastReadMessageId,
                                        Long::max
                                )
                        )
                );
        log.debug("Grouped updates: {}", updates);

        updates.forEach((userId, roomUpdates) ->
                roomUpdates.forEach((roomId, messageId) -> {
                    log.debug("Saving - userId: {}, roomId: {}, messageId: {}", userId, roomId, messageId);
                    repository.saveLastReadMessageIdInBulk(userId, roomId, messageId);
                })
        );
    }
}
