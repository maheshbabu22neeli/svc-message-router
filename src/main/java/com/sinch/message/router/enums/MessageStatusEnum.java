package com.sinch.message.router.enums;

import lombok.Getter;

@Getter
public enum MessageStatusEnum {

    PENDING,
    SENT,
    DELIVERED,
    BLOCKED,
    OPTED_OUT,
    ALREADY_OPTED_OUT;

}
