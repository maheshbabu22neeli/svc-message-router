package com.sinch.message.router.service.impl;

import com.sinch.message.router.dao.entity.MessageEntity;
import com.sinch.message.router.dao.repository.MessageRepository;
import com.sinch.message.router.dao.repository.OptOutRepository;
import com.sinch.message.router.enums.CarrierEnum;
import com.sinch.message.router.enums.OptOutEnum;
import com.sinch.message.router.enums.StatusEnum;
import com.sinch.message.router.exceptions.ResourceNotFoundException;
import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.models.OptOutResponse;
import com.sinch.message.router.utils.MessageMappingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    @Mock
    private MessageMappingUtil messageMappingUtil;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private OptOutRepository optOutRepository;

    @InjectMocks
    private MessageServiceImpl messageService;


    @Test
    void test_sendMessage_shouldReturnBlocked_whenUserIsOptedOut() {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+61411111112");
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        MessageEntity blockedEntity = new MessageEntity(messageRequest.getPhoneNumber(),
                messageRequest.getContent(), messageRequest.getFormat(), CarrierEnum.TELSTRA, StatusEnum.BLOCKED);

        MessageResponse response = new MessageResponse(blockedEntity.getId(), StatusEnum.BLOCKED);

        when(optOutRepository.isOptOut(messageRequest.getPhoneNumber())).thenReturn(true);
        when(messageMappingUtil.mapMessageRequestToMessageEntity(
                messageRequest, StatusEnum.BLOCKED)).thenReturn(blockedEntity);
        when(messageRepository.save(blockedEntity)).thenReturn(blockedEntity);
        when(messageMappingUtil.mapMessageEntityToMessageResponse(blockedEntity))
                .thenReturn(response);

        MessageResponse messageResponse = messageService.sendMessage(messageRequest);

        assertEquals(StatusEnum.BLOCKED, messageResponse.getStatus());
        assertEquals(blockedEntity.getId(), messageResponse.getId());

        verify(optOutRepository).isOptOut(messageRequest.getPhoneNumber());
        verify(messageRepository).save(blockedEntity);
    }

    @Test
    void test_sendMessage_shouldReturnPending_whenUserIsNotOptedOut() {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+61411111112");
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        MessageEntity pendingEntity = new MessageEntity(messageRequest.getPhoneNumber(),
                messageRequest.getContent(), messageRequest.getFormat(), CarrierEnum.TELSTRA, StatusEnum.PENDING);

        MessageResponse response = new MessageResponse(pendingEntity.getId(), StatusEnum.PENDING);

        when(optOutRepository.isOptOut(messageRequest.getPhoneNumber())).thenReturn(false);
        when(messageMappingUtil.mapMessageRequestToMessageEntity(
                messageRequest, StatusEnum.PENDING)).thenReturn(pendingEntity);
        when(messageRepository.save(pendingEntity)).thenReturn(pendingEntity);
        when(messageMappingUtil.mapMessageEntityToMessageResponse(pendingEntity))
                .thenReturn(response);

        MessageResponse messageResponse = messageService.sendMessage(messageRequest);

        assertEquals(StatusEnum.PENDING, messageResponse.getStatus());
        assertEquals(pendingEntity.getId(), messageResponse.getId());

        verify(optOutRepository).isOptOut(messageRequest.getPhoneNumber());
        verify(messageRepository).save(pendingEntity);
    }

    @Test
    void test_getMessage_shouldThrowResourceNotFoundException_whenMessageDoesNotExist() {

        String messageId = UUID.randomUUID().toString();

        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,
                        () -> messageService.getMessage(messageId));

        assertEquals("Message not found for id: "+messageId, exception.getMessage());

        verify(messageRepository).findById(messageId);
        verifyNoInteractions(messageMappingUtil);
    }

    @Test
    void test_getMessage_shouldReturnMessage_whenMessageExists() {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+61411111112");
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        MessageEntity messageEntity = new MessageEntity(messageRequest.getPhoneNumber(),
                messageRequest.getContent(), messageRequest.getFormat(), CarrierEnum.TELSTRA, StatusEnum.PENDING);

        MessageResponse response = new MessageResponse(messageEntity.getId(), StatusEnum.PENDING);

        when(messageRepository.findById(messageEntity.getId())).thenReturn(Optional.of(messageEntity));
        when(messageMappingUtil.mapMessageEntityToMessageResponse(messageEntity))
                .thenReturn(response);

        MessageResponse messageResult = messageService.getMessage(messageEntity.getId());

        assertNotNull(messageResult);
        assertEquals(messageEntity.getId(), messageResult.getId());
        assertEquals(StatusEnum.PENDING, messageResult.getStatus());

        verify(messageRepository).findById(messageEntity.getId());
        verify(messageMappingUtil).mapMessageEntityToMessageResponse(messageEntity);
    }

    @Test
    void test_optOut_return_Opted_out() {
        String phoneNumber = "+61411111112";

        when(optOutRepository.optOut(phoneNumber)).thenReturn(true);

        OptOutResponse optOutResponse = messageService.optOut(phoneNumber);

        assertNotNull(optOutResponse);
        assertEquals(OptOutEnum.OPTED_OUT, optOutResponse.getStatus());
    }

}
