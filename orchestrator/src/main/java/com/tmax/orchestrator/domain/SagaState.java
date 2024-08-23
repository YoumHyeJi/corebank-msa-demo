package com.tmax.orchestrator.domain;

import static com.tmax.orchestrator.saga.framework.SagaStepStatus.COMPENSATED;
import static com.tmax.orchestrator.saga.framework.SagaStepStatus.FAILED;
import static com.tmax.orchestrator.saga.framework.SagaStepStatus.STARTED;
import static com.tmax.orchestrator.saga.framework.SagaStepStatus.SUCCEEDED;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.saga.SagaType;
import com.tmax.orchestrator.saga.framework.SagaStatus;
import com.tmax.orchestrator.saga.framework.SagaStepStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.util.EnumSet;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@Entity
public class SagaState {

    @Id
    private UUID id;

    @Version
    private int version;

    /**
     * 어떤 요청으로 인해 생성된 Saga인지
     */
    @Enumerated(EnumType.STRING)
    private SagaType type;

    /**
     * 들어온 http 요청의 request body를 ObjectNode 형식으로 변환한 값
     */
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private ObjectNode payload;

    /**
     * 현재 sub step => xxxSagaStepOrder enum 값
     */
    private String currentStep;

    /**
     * key : saga의 각 step =>  DepositSagaStepOrder
     * value : 각 step의 status => SagaStepStatus
     */
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private ObjectNode stepStatus;

    /**
     * Saga의 status
     */
    @Enumerated(EnumType.STRING)
    private SagaStatus sagaStatus;

    public SagaState(SagaType sagaType, ObjectNode payload) {
        this.id = UUID.randomUUID();
        this.type = sagaType;
        this.payload = payload;
        this.sagaStatus = SagaStatus.STARTED;
        this.stepStatus = JsonNodeFactory.instance.objectNode();
    }

    public void updateCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public void updateStepStatus(String step, SagaStepStatus sagaStepStatus) {
        this.stepStatus.put(step, sagaStepStatus.name());
    }

    /**
     * Following SagaSteps To SagaStatus mapping:
     * 1. SUCCEEDED -> COMPLETED
     * 2. STARTED, SUCCEEDED -> STARTED
     * 3. FAILED, COMPENSATED -> ABORTED
     * 4. COMPENSATING, other -> ABORTING
     */
    public void advanceSagaStatus() {
        var bitmask = stepStatusToSet().stream()
            .mapToInt(status -> 1 << status.ordinal())
            .reduce(0, (a, b) -> a | b);

        // 모든 sub step이 SUCCEEDED 상태인 경우 : sagaStatus = COMPLETED
        if ((bitmask & (1 << SUCCEEDED.ordinal())) == bitmask) {
            sagaStatus = SagaStatus.COMPLETED;
        }
        // 모든 sub step이 STARTED or SUCCEEDED 상태인 경우 : sagaStatus = STARTED
        else if ((bitmask & ((1 << STARTED.ordinal()) | (1 << SUCCEEDED.ordinal()))) == bitmask) {
            sagaStatus = SagaStatus.STARTED;
        }
        // 모든 sub step이 FAILED or COMPENSATED 상태인 경우 : sagaStatus = ABORTED
        else if ((bitmask & ((1 << FAILED.ordinal()) | (1 << COMPENSATED.ordinal()))) == bitmask) {
            sagaStatus = SagaStatus.ABORTED;
        }
        // 나머지 경우 : sagaStatus = ABORTING
        else {
            sagaStatus = SagaStatus.ABORTING;
        }
    }


    /**
     * stepStatus의 value에 저장된 모든 SagaStepStatus enum 값을 EnumSet 형태로 가져오는 메서드입니다.
     *
     * @return : EnumSet<SagaStepStatus>
     */
    private EnumSet<SagaStepStatus> stepStatusToSet() {
        EnumSet<SagaStepStatus> allStatus = EnumSet.noneOf(SagaStepStatus.class);
        stepStatus.fields()
            .forEachRemaining(entry -> allStatus.add(SagaStepStatus.valueOf(entry.getValue().asText())));

        return allStatus;
    }

}
