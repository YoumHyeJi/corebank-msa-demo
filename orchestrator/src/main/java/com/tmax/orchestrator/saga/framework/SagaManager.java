package com.tmax.orchestrator.saga.framework;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmax.orchestrator.domain.SagaState;
import com.tmax.orchestrator.event.EventLogService;
import com.tmax.orchestrator.outbox.OutboxService;
import com.tmax.orchestrator.saga.SagaStateService;
import com.tmax.orchestrator.saga.SagaType;
import com.tmax.orchestrator.saga.deposit.DepositSaga;
import com.tmax.orchestrator.saga.deposit.DepositSagaStepOrder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SagaManager {

    private final SagaStateService sagaStateService;
    private final EventLogService eventLogService;
    private final OutboxService outboxService;


    /**
     * SagaState 및 DepositSaga 인스턴스를 생성하는 메서드입니다.
     * 그 후 depositSaga.init() 메서드를 호출하여, Saga를 초기화합니다.
     *
     * @param sagaType : 생성하고자 하는 Saga 타입을 입력합니다.
     * @param payload : ObjectNode 타입의 payload를 입력합니다. 해당 payload를 값을 기반으로 로직을 수행하게 됩니다.
     */
    // TODO) controller의 모든 메서드에서 공통으로 호출하는 beginSaga() 메서드를 만들고 그 안에서 분기 처리할지, 아니면 지금처럼 각각의 beginXXXSaga() 메서드를 만들지?
    public void beginDepositSaga(SagaType sagaType, ObjectNode payload){

        // 1. payload의 type을 REQUEST로 설정
        payload.put("type", PayloadType.REQUEST.name());

        // 2. SagaState를 생성하여 DB에 저장
        SagaState sagaState = new SagaState(sagaType, payload);
        sagaState.updateCurrentStep(DepositSagaStepOrder.startStep().name());
        sagaStateService.createSagaState(sagaState);

        // 3. DepositSaga 생성 및 init() 메서드 실행
        DepositSaga depositSaga = new DepositSaga(eventLogService, sagaState, outboxService);
        depositSaga.init();
    }

    /**
     * SagaState 엔티티를 조회한 후, 해당 SagaState 엔티티를 기반으로 DepositSaga 객체를 생성해서 반환하는 메서드입니다.
     *
     * @param sagaId : sagaState id를 입력합니다.
     * @return : DepositSaga 인스턴스를 생성해서 반환합니다.
     */
    // TODO) controller의 모든 메서드에서 공통으로 호출하는 findSaga() 메서드를 만들고 그 안에서 분기 처리할지, 아니면 지금처럼 각각의 findXXXSaga() 메서드를 만들지?
    public DepositSaga findDepositSaga(UUID sagaId){
        SagaState sagaState = sagaStateService.findSagaState(sagaId);

        if(sagaState == null){
            return null;
        }

        return new DepositSaga(eventLogService, sagaState, outboxService);
    }
}
