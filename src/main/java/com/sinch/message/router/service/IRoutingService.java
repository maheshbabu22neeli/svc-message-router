package com.sinch.message.router.service;

import com.sinch.message.router.enums.CarrierEnum;

public interface IRoutingService {

    CarrierEnum getCarrierEnumByPhoneNumber(String phoneNumber);

}
