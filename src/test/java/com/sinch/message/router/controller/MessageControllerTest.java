package com.sinch.message.router.controller;

import com.sinch.message.router.dao.repository.OptOutRepository;
import com.sinch.message.router.enums.MessageStatusEnum;
import com.sinch.message.router.models.MessageRequest;
import com.sinch.message.router.models.MessageResponse;
import com.sinch.message.router.service.IMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OptOutRepository optOutRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IMessageService messageService;

    @BeforeEach
    void setup() {

    }

    @Test
    void test_sendMessage_missing_all_fields() throws Exception {
        MessageRequest messageRequest = new MessageRequest();

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage", containsString("phoneNumber is required")))
                .andExpect(jsonPath("$.errorMessage", containsString("content cannot be blank")))
                .andExpect(jsonPath("$.errorMessage", containsString("Format is required")));
    }

    @Test
    void test_sendMessage_missing_phoneNumber() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "[phoneNumber: phoneNumber is required]"));
    }

    @Test
    void test_sendMessage_missing_content() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+61411111112");
        messageRequest.setFormat("SMS");

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "[content: content cannot be blank]"));
    }

    @Test
    void test_sendMessage_invalid_phoneNumber_no_plus_symbol() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("61411111112");    // no + symbol
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "[phoneNumber: phone number must start with +(country code with 2 digits)" +
                                "(phone number with 9 digits)]"));
    }

    @Test
    void test_sendMessage_invalid_phoneNumber_no_matching_length() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+614111111");    // no matching length
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "[phoneNumber: phone number must start with +(country code with 2 digits)" +
                                "(phone number with 9 digits)]"));
    }

    @Test
    void test_sendMessage_success_response() throws Exception {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setPhoneNumber("+61411111112");    // no matching length
        messageRequest.setContent("Hello World");
        messageRequest.setFormat("SMS");

        MessageResponse messageResponse = new MessageResponse(UUID.randomUUID().toString(), MessageStatusEnum.PENDING);

        when(messageService.sendMessage(any())).thenReturn(messageResponse);

        mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(messageResponse.getId()))
                .andExpect(jsonPath("$.status").value(MessageStatusEnum.PENDING.toString()));
    }

    @Test
    void test_getMessage_failure_response_missing_id() throws Exception {

        mockMvc.perform(get("/v1/messages/{id}", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "No static resource v1/messages for request '/v1/messages/'."));
    }

    @Test
    void test_getMessage_failure_response_when_id_empty_string() throws Exception {

        mockMvc.perform(get("/v1/messages/{id}", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "id cannot be null or empty"));
    }

    @Test
    void test_getMessage_success_response() throws Exception {

        MessageResponse messageResponse =
                new MessageResponse(UUID.randomUUID().toString(), MessageStatusEnum.DELIVERED);

        when(messageService.getMessage(any())).thenReturn(messageResponse);

        mockMvc.perform(get("/v1/messages/{id}", messageResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageResponse.getId()))
                .andExpect(jsonPath("$.status").value(MessageStatusEnum.DELIVERED.toString()));
    }

    @Test
    void test_sendMessage_toOptedOutNumber_shouldBeBlocked() throws Exception {

        String phoneNumber = "+61412345678";
        optOutRepository.optOut(phoneNumber);

        MessageRequest request = new MessageRequest();
        request.setPhoneNumber(phoneNumber);
        request.setContent("Hello World");
        request.setFormat("SMS");

        MessageResponse sendMessageResponse = new MessageResponse(UUID.randomUUID().toString(), MessageStatusEnum.BLOCKED);

        when(messageService.sendMessage(any())).thenReturn(sendMessageResponse);

        MvcResult sendResult = mockMvc.perform(post("/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(MessageStatusEnum.BLOCKED.toString()))
                .andReturn();

        String sendMessageResponseBody = sendResult.getResponse().getContentAsString();
        MessageResponse getMessageResponse =
                objectMapper.readValue(sendMessageResponseBody, MessageResponse.class);

        when(messageService.getMessage(getMessageResponse.getId())).thenReturn(getMessageResponse);

        mockMvc.perform(get("/v1/messages/{id}", getMessageResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(MessageStatusEnum.BLOCKED.toString()));
    }

}
