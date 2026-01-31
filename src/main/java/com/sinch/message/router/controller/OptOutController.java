package com.sinch.message.router.controller;

import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class OptOutController {

    private final IMessageService messageService;

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<MessageResponse> optOut(@PathVariable String phoneNumber) {
        log.info("Request received to optOut a phone number");

        MessageResponse messageResponse = messageService.optOut(phoneNumber);

        log.info("Response sent for optOut a phone number");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageResponse);
    }

}
