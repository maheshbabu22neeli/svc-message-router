package com.sinch.message.router.service.impl;

import com.sinch.message.router.dao.entity.MessageEntity;
import com.sinch.message.router.dao.repository.MessageRepository;
import com.sinch.message.router.dao.repository.OptOutRepository;
import com.sinch.message.router.enums.MessageStatusEnum;
import com.sinch.message.router.exceptions.ResourceNotFoundException;
import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.service.IMessageService;
import com.sinch.message.router.utils.MessageMappingUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageMappingUtil messageMappingUtil;

    private final MessageRepository messageRepository;

    private final OptOutRepository optOutRepository;

    @Override
    public MessageResponse sendMessage(final MessageRequest messageRequest) {

        MessageEntity messageEntity;
        if (optOutRepository.isOptOut(messageRequest.getPhoneNumber())) {
            messageEntity =
                    messageRepository.save(messageMappingUtil.mapMessageRequestToMessageEntity(messageRequest,
                    MessageStatusEnum.BLOCKED));
            return messageMappingUtil.mapMessageEntityToMessageResponse(messageEntity);
        }

        messageEntity =
                messageRepository.save(messageMappingUtil.mapMessageRequestToMessageEntity(messageRequest,
                        MessageStatusEnum.PENDING));
        return messageMappingUtil.mapMessageEntityToMessageResponse(messageEntity);
    }

    @Override
    public MessageResponse getMessage(final String id) {

        MessageEntity messageEntity = messageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Message not found for id: " + id));

        return messageMappingUtil.mapMessageEntityToMessageResponse(messageEntity);
    }

    @Override
    public MessageResponse optOut(final String phoneNumber) {
        if (optOutRepository.optOut(phoneNumber)) {
            return new MessageResponse(MessageStatusEnum.OPTED_OUT);
        } else {
            return new MessageResponse(MessageStatusEnum.ALREADY_OPTED_OUT);
        }
    }

    @Override
    public List<MessageResponse> getAllMessages() {
        List<MessageEntity> messageEntityList = messageRepository.findAll();
        return messageMappingUtil.mapAllEntitiesToResponse(messageEntityList);
    }

}
