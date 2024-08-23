package com.tmax.orchestrator.saga.deposit;


import static com.tmax.orchestrator.saga.deposit.DepositSagaStepOrder.ACCOUNT_BALANCE_UPDATE;
import static com.tmax.orchestrator.saga.deposit.DepositSagaStepOrder.TRANSACTION_INSERT;
import static com.tmax.orchestrator.saga.framework.PayloadType.CANCEL;
import static com.tmax.orchestrator.saga.framework.PayloadType.REQUEST;

import com.tmax.orchestrator.domain.Outbox;
import com.tmax.orchestrator.domain.SagaState;
import com.tmax.orchestrator.event.EventLogService;
import com.tmax.orchestrator.outbox.OutboxService;
import com.tmax.orchestrator.saga.framework.Saga;
import com.tmax.orchestrator.saga.framework.SagaStepStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Saga 클래스를 상속받는 하위 클래스이다.
 * DepositSaga 클래스에서는 입금 실행에 관한 각 서브 스탭의 상태를 추적하고 관리합니다.
 */
public final class DepositSaga extends Saga {

    private final SagaState sagaState;
    private final OutboxService outboxService;

    public DepositSaga(EventLogService eventLogService,
        SagaState sagaState,
        OutboxService outboxService) {
        super(eventLogService);
        this.sagaState = sagaState;
        this.outboxService = outboxService;
    }

    /**
     * DepositSaga를 초기화하는 메서드입니다.
     * advanced() 메서드를 호출합니다.
     */
    public void init() {
        advanced();
    }

    /**
     * 현재 Saga Sub Step의 상태에 따라 다음 Saga Sub Step을 진행하거나, 이전 Saga Sub Step으로 롤백하는 보상 트랜잭션을 수행합니다.
     *
     * @param currentStep : 현재 Saga Sub Step을 입력합니다.
     * @param stepStatus : 현재 Saga Sub Step의 상태를 입력합니다.
     */
    private void onStepEvent(DepositSagaStepOrder currentStep, SagaStepStatus stepStatus) {

        // 1. sagaState의 stepStatus를 update
        sagaState.updateStepStatus(currentStep.name(), stepStatus);

        // 2-1. 현재 step의 status가 성공인 경우
        if (stepStatus.isSucceeded()) {
            advanced();
        }
        // 2-2. 현재 step의 status가 실패인 경우
        else if (stepStatus.isFailedOrCompensated()) {
            goBack();
        }

        // 3. sagaState의 sagaStatus를 업데이트
        sagaState.advanceSagaStatus();
    }

    /**
     * Saga.ensureProcessed() 메서드를 호출하여, DB에 EventLog 엔티티를 저장하고 callback 메서드인 onStepEvent()를 실행합니다.
     *
     * @param eventId : Event id를 입력합니다.
     * @param payload : AccountBalanceUpdateEvent 타입의 payload를 입력합니다.
     */
    public void onAccountBalanceUpdateEvent(UUID eventId, AccountBalanceUpdateEvent payload) {
        ensureProcessed(eventId, () -> {
            onStepEvent(ACCOUNT_BALANCE_UPDATE, payload.status().toSagaStepStatus());
        });
    }

    /**
     * Saga.ensureProcessed() 메서드를 호출하여, DB에 EventLog 엔티티를 저장하고 callback 메서드인 onStepEvent()를 실행합니다.
     *
     * @param eventId : Event id를 입력합니다.
     * @param payload : TransactionInsertEvent 타입의 payload를 입력합니다.
     */
    public void onTransactionInsertEvent(UUID eventId, TransactionInsertEvent payload) {
        ensureProcessed(eventId, () -> {
            onStepEvent(TRANSACTION_INSERT, payload.status().toSagaStepStatus());
        });
    }


    /**
     * 다음 Sub Step을 진행하기 위해 Outbox 테이블에 메시지를 발행하고, SagaState 엔티티의 currentStep 및 stepStatus를 업데이트하는 메서드입니다.
     *
     * 1. 다음 DepositSagaStepOrder 찾기
     * 2. Outbox 테이블에 새로운 메시지 insert
     * 3. SagaState update
     */
    private void advanced() {
        // 1. 다음 DepositSagaStepOrder 찾기
        DepositSagaStepOrder nextStep = null;
        if (sagaState.getCurrentStep() != null){
            DepositSagaStepOrder currentStep = DepositSagaStepOrder.valueOf(
                sagaState.getCurrentStep());
            nextStep = currentStep.nextStep();
        }

        // 현재 스탭이 마지막 스탭인 경우
        if (nextStep == null) {
            sagaState.updateCurrentStep(null);
            return;
        }

        // 2. Outbox 테이블에 새로운 메시지 insert
        Outbox outbox = Outbox.builder()
            .id(UUID.randomUUID())
            .aggregateId(sagaState.getId().toString())      // sagaId
            .aggregateType(nextStep.name())
            .type(REQUEST.name())                           // eventType
            .payload(sagaState.getPayload())
            .timestamp(LocalDateTime.now())
            .build();
        outboxService.createOutbox(outbox);

        // 3. SagaState update
        sagaState.updateStepStatus(nextStep.name(), SagaStepStatus.STARTED);
        sagaState.updateCurrentStep(nextStep.name());
    }

    /**
     * 이전 Sub Step으로 롤백하기 위해 Outbox 테이블에 메시지를 발행하고, SagaState 엔티티의 currentStep 및 stepStatus를 업데이트하는 메서드입니다.
     *
     * 1. 이전 DepositSagaStepOrder 찾기
     * 2. Outbox 테이블에 새로운 메시지 insert
     * 3. SagaState update
     */
    private void goBack() {
        // 1. 이전 DepositSagaStepOrder 찾기
        DepositSagaStepOrder previousStep = null;
        if (sagaState.getCurrentStep() != null){
            DepositSagaStepOrder currentStep = DepositSagaStepOrder.valueOf(
                sagaState.getCurrentStep());
            previousStep = currentStep.previousStep();;
        }

        // 현재 스탭이 처음 스탭인 경우
        if (previousStep == null) {
            sagaState.updateCurrentStep(null);
            return;
        }

        // 2. Outbox 테이블에 새로운 메시지 insert
        Outbox outbox = Outbox.builder()
            .id(UUID.randomUUID())
            .aggregateId(sagaState.getId().toString())      // sagaId
            .aggregateType(previousStep.name())
            .type(CANCEL.name())                            // eventType
            .payload(sagaState.getPayload())
            .timestamp(LocalDateTime.now())
            .build();
        outboxService.createOutbox(outbox);

        // 3. SagaState update
        sagaState.updateStepStatus(previousStep.name(), SagaStepStatus.COMPENSATING);
        sagaState.updateCurrentStep(previousStep.name());
    }


}
