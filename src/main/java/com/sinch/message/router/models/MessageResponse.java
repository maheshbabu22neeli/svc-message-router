package com.sinch.message.router.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sinch.message.router.enums.CarrierEnum;
import com.sinch.message.router.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse {

    private String id;
    private String phoneNumber;
    private StatusEnum status;
    private CarrierEnum carrierName;

    public MessageResponse(String id, StatusEnum status) {
        this.id = id;
        this.status = status;
    }

    public MessageResponse(StatusEnum status) {
        this.status = status;
    }

    public MessageResponse(String id, StatusEnum status, CarrierEnum carrierName) {
        this.id = id;
        this.status = status;
        this.carrierName = carrierName;
    }
}
