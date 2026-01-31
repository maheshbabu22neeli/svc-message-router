package com.sinch.message.router.service;

import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;

import java.util.List;

public interface IMessageService {

    MessageResponse sendMessage(final MessageRequest messageRequest);

    MessageResponse getMessage(final String id);

    MessageResponse optOut(final String phoneNumber);

    List<MessageResponse> getAllMessages();

}
