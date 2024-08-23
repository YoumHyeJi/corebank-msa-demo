package com.tmax.orchestrator.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.orchestrator.saga.deposit.AccountBalanceUpdateEvent;
import com.tmax.orchestrator.saga.deposit.TransactionInsertEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Kafka Topic에 publish 된 메시지를 consume하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrchestratorConsumer {

    private final DepositEventHandler depositEventHandler;
    private final ObjectMapper objectMapper;

    public static final String EVENT_ID = "id";
    public static final String EVENT_TYPE = "eventType";

    @KafkaListener(topics = "ORCHESTRATOR_TOPIC", groupId = "orchestator-group")
    public void consume(String message) {
        log.info(message);
    }


    /**
     * kafka topic인 "transaction-insert.outbox.events"에 publish된 메시지를 consume하는 Listener입니다.
     * 다른 micro service에서 처리한 이벤트를 핸들링하기 위해, depositEventHandler.onAccountBalanceUpdateEvent() 메서드를 호출합니다.
     * 이때 consume한 메시지에 저장돼 있는 데이터를 기반으로 AccountBalanceUpdateEvent를 생성하고, depositEventHandler.onAccountBalanceUpdateEvent() 메서드의 파라미터로 넘깁니다.
     *
     * @param sagaId : SagaState id를 입력합니다.
     * @param eventId : Event id를 입력합니다.
     * @param eventType : Event type을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "${kafka.topic.saga.deposit.account-balance-update.inbox.events}")
    void listenAccountBalanceUpdateEvent(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String eventId,
        @Header(EVENT_TYPE) String eventType,
        @Payload String payload) throws JsonProcessingException {

        log.info("Kafka message with key = {}, eventId = {}, eventType = {} payload = {}", sagaId, eventId, eventType, payload);
        AccountBalanceUpdateEvent event = objectMapper.readValue(payload, AccountBalanceUpdateEvent.class);
        depositEventHandler.onAccountBalanceUpdateEvent(sagaId, UUID.fromString(eventId), event);
    }

    /**
     * kafka topic인 "account-balance-update.outbox.events"에 publish된 메시지를 consume하는 Listener입니다.
     * 다른 micro service에서 처리한 이벤트를 핸들링하기 위해, depositEventHandler.onTransactionInsertEvent() 메서드를 호출합니다.
     * 이때 consume한 메시지에 저장돼 있는 데이터를 기반으로 TransactionInsertEvent를 생성하고, depositEventHandler.onTransactionInsertEvent() 메서드의 파라미터로 넘깁니다.
     *
     * @param sagaId : SagaState id를 입력합니다.
     * @param eventId : Event id를 입력합니다.
     * @param eventType : Event type을 입력합니다.
     * @param payload : payload를 입력합니다.
     */
    @KafkaListener(topics = "${kafka.topic.saga.deposit.transaction-insert.inbox.events}")
    void listenTransactionInsertEvent(
        @Header(KafkaHeaders.RECEIVED_KEY) UUID sagaId,
        @Header(EVENT_ID) String eventId,
        @Header(EVENT_TYPE) String eventType,
        @Payload String payload) throws JsonProcessingException {

        log.info("Kafka message with key = {}, eventId = {}, eventType = {} payload = {}", sagaId, eventId, eventType, payload);
        TransactionInsertEvent event = objectMapper.readValue(payload, TransactionInsertEvent.class);
        depositEventHandler.onTransactionInsertEvent(sagaId, UUID.fromString(eventId), event);
    }
}
