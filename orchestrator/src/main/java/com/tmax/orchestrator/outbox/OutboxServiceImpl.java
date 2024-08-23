package com.tmax.orchestrator.outbox;

import com.tmax.orchestrator.domain.Outbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OutboxServiceImpl implements OutboxService{


    private final OutboxRepository outboxRepository;

    /**
     * 입력으로 받은 Outbox 엔티티를 DB에 저장하는 메서드입니다.
     *
     * Outbox 테이블에 새로운 데이터가 insert되면 Debezium이 해당 변경사항을 캡처하여(CDC) kafka의 topic으로 메시지를 publish 합니다.
     *
     * kafka topic 이름은 orchestrator-connector.json 파일의 "transforms.outbox.route.topic.replacement" 설정에 의해 정해집니다.
     * ${routedByValue}은 Outbox 엔티티의 aggregateType 컬럼에 해당합니다.
     * 따라서 kafka topic 이름은 ${aggregateType}.inbox.events 입니다.
     *
     * @param outbox : 저장하고자 하는 Outbox 엔티티를 입력합니다.
     */
    @Override
    public void createOutbox(Outbox outbox) {
        outboxRepository.save(outbox);
    }
}
