package com.sinch.message.router.controller;

import ch.qos.logback.core.util.StringUtil;
import com.sinch.message.router.exceptions.ValidationException;
import com.sinch.message.router.models.OptOutResponse;
import com.sinch.message.router.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tools.jackson.databind.ObjectMapper;

import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class OptOutController {

    private static final Pattern PHONE_REGEX = Pattern.compile("^\\+\\d{11}$");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final IMessageService messageService;

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<OptOutResponse> optOut(@PathVariable String phoneNumber) {
        log.info("Request received to optOut a phone number: {}", phoneNumber);

        validatePhoneNumber(phoneNumber);

        OptOutResponse optOutResponse = messageService.optOut(phoneNumber);

        log.info("Response sent for optOut a phone number: {}", objectMapper.writeValueAsString(optOutResponse));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(optOutResponse);
    }

    private void validatePhoneNumber(final String phoneNumber) {

        if (phoneNumber == null || StringUtil.isNullOrEmpty(phoneNumber.trim())) {
            throw new ValidationException("phoneNumber cannot be null or empty");
        }

        if (!PHONE_REGEX.matcher(phoneNumber).matches()) {
            throw new ValidationException(
                    "phone number must start with +(country code with 2 digits)(phone number with 9 digits)");
        }

    }

}
