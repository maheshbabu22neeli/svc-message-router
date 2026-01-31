package com.sinch.message.router.utils;

import com.sinch.message.router.dao.entity.MessageEntity;
import com.sinch.message.router.enums.MessageStatusEnum;
import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.service.IRoutingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MessageMappingUtil {

    private final IRoutingService iRoutingService;

    public MessageEntity mapMessageRequestToMessageEntity(MessageRequest messageRequest,
                                                          MessageStatusEnum messageStatusEnum) {
        return new MessageEntity(messageRequest.getPhoneNumber(),
                messageRequest.getContent(),
                messageRequest.getFormat(),
                iRoutingService.getCarrierEnumByPhoneNumber(messageRequest.getPhoneNumber()),
                messageStatusEnum);
    }

    public MessageResponse mapMessageEntityToMessageResponse(MessageEntity messageEntity) {
        return new MessageResponse(messageEntity.getId(), messageEntity.getStatus());
    }

    public MessageResponse mapMessageEntityToMessageResponseWithCarrierName(MessageEntity messageEntity) {
        return new MessageResponse(messageEntity.getId(), messageEntity.getStatus(), messageEntity.getCarrierName());
    }

    public List<MessageResponse> mapAllEntitiesToResponse(List<MessageEntity> messageEntityList) {
        if (messageEntityList == null || messageEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return messageEntityList.stream()
                .map(this::mapMessageEntityToMessageResponseWithCarrierName)
                .collect(Collectors.toList());
    }
}
