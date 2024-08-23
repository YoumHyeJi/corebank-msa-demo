package org.tmax.banking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountConsumer {

    @KafkaListener(topics = "ACCOUNT_TOPIC", groupId = "account-group")
    public void consume(String message) {
        log.info(message);
    }
}
