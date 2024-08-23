package org.tmax.account.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankingConsumer {

    @KafkaListener(topics = "BANKING_TOPIC", groupId = "banking-group")
    public void consume(String message) {
        log.info(message);
    }
}
