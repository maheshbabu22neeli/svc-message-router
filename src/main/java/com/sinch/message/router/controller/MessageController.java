package com.sinch.message.router.controller;

import ch.qos.logback.core.util.StringUtil;
import com.sinch.message.router.exceptions.ValidationException;
import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.service.IMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody @Valid MessageRequest messageRequest) {
        log.info("Request received to sent message: {}", objectMapper.writeValueAsString(messageRequest));

        MessageResponse messageResponse = messageService.sendMessage(messageRequest);

        log.info("Response sent for sent message: {}", objectMapper.writeValueAsString(messageResponse));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageResponse);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageResponse> getMessage(
            @PathVariable String id) {
        log.info("Request received to get message by id: {}", id);

        if (id == null || StringUtil.isNullOrEmpty(id.trim()))  {
            throw new ValidationException("id cannot be null");
        }

        MessageResponse messageResponse = messageService.getMessage(id);

        log.info("Response sent for get message by id: {}", objectMapper.writeValueAsString(messageResponse));
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageResponse>> getMessage() {
        log.info("Request received to get all messages");

        List<MessageResponse> messageResponseList = messageService.getAllMessages();

        log.info("Response sent to get all messages {}", objectMapper.writeValueAsString(messageResponseList));
        return ResponseEntity.ok(messageResponseList);
    }
}
