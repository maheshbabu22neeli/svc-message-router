package com.sinch.message.router.dao.repository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OptOutRepository {

    private final Set<String> phoneNumberSet = new HashSet<>();

    public boolean optOut(String phoneNumber) {
        if (isOptOut(phoneNumber)) {       // already Opted Out
            return false;
        } else {
            phoneNumberSet.add(phoneNumber);   // newly Opted Out
            return true;
        }
    }

    public boolean isOptOut(String phoneNumber) {
        return phoneNumberSet.contains(phoneNumber);
    }

}
