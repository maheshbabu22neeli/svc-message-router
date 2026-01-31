package com.sinch.message.router.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ErrorMessageResponse {

    private int statusCode;
    private String errorMessage;

}
