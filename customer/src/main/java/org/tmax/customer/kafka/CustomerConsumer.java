package org.tmax.customer.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerConsumer {

    @KafkaListener(topics = "CUSTOMER_TOPIC", groupId = "customer-group")
    public void consume(String message) {
        log.info(message);
    }
}
