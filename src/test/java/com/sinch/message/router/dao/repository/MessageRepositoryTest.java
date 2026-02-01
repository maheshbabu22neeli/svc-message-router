package com.sinch.message.router.dao.repository;

import com.sinch.message.router.dao.entity.MessageEntity;
import com.sinch.message.router.enums.CarrierEnum;
import com.sinch.message.router.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageRepositoryTest {

    private MessageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MessageRepository();
    }

    @Test
    void test_save_shouldStore_And_ReturnMessage() {

        MessageEntity messageEntity = getMessageEntity(CarrierEnum.OPTUS, StatusEnum.PENDING);
        MessageEntity savedMessageEntity = repository.save(messageEntity);

        assertEquals(messageEntity, savedMessageEntity);
        assertEquals(messageEntity.getPhoneNumber(), savedMessageEntity.getPhoneNumber());
        assertEquals(messageEntity.getContent(), savedMessageEntity.getContent());
        assertEquals(messageEntity.getStatus(), savedMessageEntity.getStatus());
        assertEquals(messageEntity.getFormat(), savedMessageEntity.getFormat());
        assertEquals(messageEntity.getCarrierName(), savedMessageEntity.getCarrierName());

        assertTrue(repository.findById(messageEntity.getId()).isPresent());
    }

    @Test
    void test_findAll_shouldReturn_All_SavedMessages() {

        repository.save(getMessageEntity(CarrierEnum.TELSTRA, StatusEnum.DELIVERED));
        repository.save(getMessageEntity(CarrierEnum.SPARK, StatusEnum.BLOCKED));
        repository.save(getMessageEntity(CarrierEnum.OPTUS, StatusEnum.SENT));

        List<MessageEntity> messageEntityList = repository.findAll();

        assertEquals(3, messageEntityList.size());
    }

    @Test
    void test_findByStatus_return_saved_message() {

        repository.save(getMessageEntity(CarrierEnum.TELSTRA, StatusEnum.DELIVERED));
        repository.save(getMessageEntity(CarrierEnum.OPTUS, StatusEnum.DELIVERED));
        repository.save(getMessageEntity(CarrierEnum.SPARK, StatusEnum.BLOCKED));
        repository.save(getMessageEntity(CarrierEnum.SPARK, StatusEnum.DELIVERED));
        repository.save(getMessageEntity(CarrierEnum.OPTUS, StatusEnum.SENT));
        repository.save(getMessageEntity(CarrierEnum.OPTUS, StatusEnum.DELIVERED));

        List<MessageEntity> messageEntityList = repository.findAllByStatus(StatusEnum.DELIVERED);

        assertEquals(4, messageEntityList.size());
    }

    private MessageEntity getMessageEntity(CarrierEnum carrierName, StatusEnum status) {
        return new MessageEntity("+61411111112",
                "Hello World, This is an assignment",
                "SMS", carrierName, status);
    }

}
