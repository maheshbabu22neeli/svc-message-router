package com.sinch.message.router.dao.repository;

import com.sinch.message.router.dao.entity.MessageEntity;
import com.sinch.message.router.enums.MessageStatusEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MessageRepository {

    private final Map<String, MessageEntity> messageEntityMap = new HashMap<>();

    public MessageEntity save(MessageEntity messageEntity) {
        messageEntityMap.put(messageEntity.getId(), messageEntity);
        return messageEntity;
    }

    public Optional<MessageEntity> findById(String id) {
        return Optional.ofNullable(messageEntityMap.get(id));
    }

    public List<MessageEntity> findAll() {
        return List.copyOf(messageEntityMap.values());
    }

    public List<MessageEntity> findAllByStatus(MessageStatusEnum messageStatusEnum) {
        return  messageEntityMap.values()
                        .stream()
                        .filter(m -> m.getStatus() == messageStatusEnum)
                        .toList();
    }

}
