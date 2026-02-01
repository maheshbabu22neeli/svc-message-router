package com.sinch.message.router.service.impl;

import com.sinch.message.router.enums.CarrierEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RoutingServiceImplTest {

    private RoutingServiceImpl routingService;

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl();
    }

    @Test
    void test_getCarrierEnumByPhoneNumber_shouldReturnTelstra_for_AU_Numbers() {

        CarrierEnum carrier =
                routingService.getCarrierEnumByPhoneNumber("+61412345678");

        assertTrue(Objects.equals(carrier, CarrierEnum.TELSTRA) ||
                Objects.equals(carrier, CarrierEnum.OPTUS));
    }

    @Test
    void test_getCarrierEnumByPhoneNumber_shouldReturnSpark_for_AZ_Numbers() {

        CarrierEnum carrier =
                routingService.getCarrierEnumByPhoneNumber("+64412345678");

        assertEquals(CarrierEnum.SPARK, carrier);
    }

    @Test
    void test_getCarrierEnumByPhoneNumber_shouldReturnGlobal_for_Other_Numbers() {

        CarrierEnum carrier =
                routingService.getCarrierEnumByPhoneNumber("+65412345678");

        assertEquals(CarrierEnum.GLOBAL, carrier);
    }

}
