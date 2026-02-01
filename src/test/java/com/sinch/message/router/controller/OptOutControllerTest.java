package com.sinch.message.router.controller;

import com.sinch.message.router.enums.OptOutEnum;
import com.sinch.message.router.models.OptOutResponse;
import com.sinch.message.router.service.IMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OptOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IMessageService messageService;

    @BeforeEach
    void setup() {

    }

    @Test
    void test_optOut_failure_response_missing_phoneNumber() throws Exception {

        mockMvc.perform(post("/v1/optout/{phoneNumber}", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "No static resource v1/optout for request '/v1/optout/'."));
    }

    @Test
    void test_optOut_failure_response_empty_phoneNumber() throws Exception {

        mockMvc.perform(post("/v1/optout/{phoneNumber}", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "phoneNumber cannot be null or empty"));
    }

    @Test
    void test_optOut_failure_response_missing_plus_symbol_phoneNumber() throws Exception {

        mockMvc.perform(post("/v1/optout/{phoneNumber}", "61411111112"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "phone number must start with +(country code with 2 digits)" +
                                "(phone number with 9 digits)"));
    }

    @Test
    void test_optOut_failure_response_length_invalid_phoneNumber() throws Exception {

        mockMvc.perform(post("/v1/optout/{phoneNumber}", "61411111"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value(
                        "phone number must start with +(country code with 2 digits)" +
                                "(phone number with 9 digits)"));
    }

    @Test
    void test_optOut_success_response() throws Exception {

        OptOutResponse optOutResponse =
                new OptOutResponse(OptOutEnum.OPTED_OUT);

        when(messageService.optOut(any())).thenReturn(optOutResponse);

        mockMvc.perform(post("/v1/optout/{phoneNumber}", "+61411111112"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(OptOutEnum.OPTED_OUT.toString()));
    }

}
