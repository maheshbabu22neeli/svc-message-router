package com.sinch.message.router.service.impl;

import com.sinch.message.router.enums.CarrierEnum;
import com.sinch.message.router.service.IRoutingService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoutingServiceImpl implements IRoutingService {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public CarrierEnum getCarrierEnumByPhoneNumber(String phoneNumber) {

        String countryCode = getCountryCode(phoneNumber);

        return switch (countryCode) {
            case "+61" -> atomicInteger.incrementAndGet() % 2 == 0
                    ? CarrierEnum.TELSTRA : CarrierEnum.OPTUS;
            case "+64" -> CarrierEnum.SPARK;
            default -> CarrierEnum.GLOBAL;
        };
    }

    private String getCountryCode(String phoneNumber) {
        return phoneNumber.substring(0, 3);
    }
}
