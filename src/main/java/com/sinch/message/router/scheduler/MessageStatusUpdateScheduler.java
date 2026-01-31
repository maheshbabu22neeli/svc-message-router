package com.sinch.message.router.scheduler;

import com.sinch.message.router.dao.repository.MessageRepository;
import com.sinch.message.router.enums.MessageStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageStatusUpdateScheduler {

    private final MessageRepository messageRepository;

    @Scheduled(fixedRateString = "${message.scheduler.fixed-rate-ms}")
    public void updateMessageStatuses() {
        log.info("Running background job to update message statuses...");

        messageRepository.findAll().forEach(message -> {
            // Only update PENDING messages
            if (message.getStatus() == MessageStatusEnum.PENDING) {
                long numericId = getNumericValue(message.getId());
                if (numericId % 2 == 0) {
                    message.setStatus(MessageStatusEnum.DELIVERED);
                } else {
                    message.setStatus(MessageStatusEnum.SENT);
                }
                log.info("Updated message {} to status {}", message.getId(), message.getStatus());
            }
        });
    }

    private long getNumericValue(String uuid) {
        // Convert UUID string to numeric long using least significant bits
        return Math.abs(java.util.UUID.fromString(uuid).getLeastSignificantBits());
    }

}
