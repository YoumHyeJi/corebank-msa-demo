package com.tmax.orchestrator.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


/**
 * kafka에 메시지를 발행하기 위해 필요한 데이터를 저장하는 테이블.
 * outbox 테이블에 새로운 row가 insert되면 Debezium이 해당 insert 로그를 읽어서 kafka에 메시지를 발행한다.
 *
 * [kafka message]
 * kafka message는 key, value, header로 구성되며 각각 다음과 같이 정의된다.
 * 1) key = Outbox 엔티티의 aggregateId 필드
 * 2) value = Outbox 엔티티의 payload 필드
 * 3) header = {
 *              "id" : 새로 생성된 event id,
 *              "eventType" : Outbox 엔티티의 type 필드
 *             }
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime timestamp;

    /**
     * aggregateId == sagaStateId
     */
    @Column(name = "aggregateid", unique = true)
    private String aggregateId;

    /**
     * aggregateType == SagaStepOrder
     *
     * 이 값의 의해서 토픽이 생성된다.
     * aggregateType = ${routedByValue}
     */
    @Column(name = "aggregatetype")
    private String aggregateType;

    /**
     * type == PayloadType
     */
    private String type;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private ObjectNode payload;

}
