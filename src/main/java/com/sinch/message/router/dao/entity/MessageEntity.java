package com.sinch.message.router.dao.entity;

import com.sinch.message.router.enums.CarrierEnum;
import com.sinch.message.router.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class MessageEntity {

    private String id;
    private String phoneNumber;
    private String content;
    private String format;
    private CarrierEnum carrierName;
    private StatusEnum status;

    public MessageEntity(String phoneNumber, String content, String format, CarrierEnum carrierName,
                         StatusEnum status) {
        this.id = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.format = format;
        this.carrierName = carrierName;
        this.status = status;
    }

}
