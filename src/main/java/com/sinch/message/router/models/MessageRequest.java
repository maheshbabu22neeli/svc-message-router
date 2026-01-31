package com.sinch.message.router.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageRequest {

    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^\\+\\d{11}$",
            message = "phone number must start with +(country code with 2 digits)(phone number with 9 digits)")
    private String phoneNumber;

    @NotBlank(message = "content cannot be blank")
    @Size(max = 160, message = "content cannot exceed 160 characters")
    private String content;

    @NotBlank(message = "Format is required")
    private String format;
}
