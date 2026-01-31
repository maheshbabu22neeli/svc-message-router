package com.sinch.message.router.enums;

import lombok.Getter;

@Getter
public enum CarrierEnum {

    TELSTRA("+61", "Telstra"),
    OPTUS("+61", "Optus"),
    SPARK("+64", "Spark"),
    GLOBAL("OTHER", "Global");

    private final String countryCode;
    private final String carrierName;

    CarrierEnum(String countryCode, String carrierName) {
        this.countryCode = countryCode;
        this.carrierName = carrierName;
    }

}
