package com.tmax.orchestrator.kafka;

import com.tmax.orchestrator.saga.deposit.AccountBalanceUpdateEvent;
import com.tmax.orchestrator.saga.deposit.DepositSaga;
import com.tmax.orchestrator.saga.deposit.TransactionInsertEvent;
import com.tmax.orchestrator.saga.framework.SagaManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 입금 실행 관련하여 발생한 이벤트를 핸들링하는 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class DepositEventHandler {

    private final SagaManager sagaManager;


    /**
     * 1) sagaManager.findDepositSaga() 메서드를 호출하여, sagaId로 SagaState 엔티티를 조회하고 이를 기반으로 DepositSaga 인스턴스를 생성합니다.
     * 2-1) 반환된 DepositSaga가 null 이면 그대로 return 합니다.
     * 2-2) 반환된 DepositSaga가 null이 아니면 depositSaga.onAccountBalanceUpdateEvent() 메서드를 호출합니다.
     *    해당 메서드에서 현재 발생한 이벤트의 status에 따라 다음 sub step을 진행하거나, 이전 sub step으로 롤백하는 보상 트랜잭션을 실행합니다.
     *
     * @param sagaId : SagaState id를 입력합니다.
     * @param eventId : Event id를 입력합니다.
     * @param payload : AccountBalanceUpdateEvent 타입의 payload를 입력합니다.
     */
    @Transactional
    public void onAccountBalanceUpdateEvent(UUID sagaId, UUID eventId, AccountBalanceUpdateEvent payload){
        DepositSaga depositSaga = sagaManager.findDepositSaga(sagaId);

        // 반환된 depositSaga가 null 이면 그대로 return 합니다.
        if (depositSaga == null) {
            return;
        }

        depositSaga.onAccountBalanceUpdateEvent(eventId, payload);
    }

    /**
     * 1) sagaManager.findDepositSaga() 메서드를 호출하여, sagaId로 SagaState 엔티티를 조회하고 이를 기반으로 DepositSaga 인스턴스를 생성합니다.
     * 2-1) 반환된 DepositSaga가 null 이면 그대로 return 합니다.
     * 2-2) 반환된 DepositSaga가 null이 아니면 depositSaga.onTransactionInsertEvent() 메서드를 호출합니다.
     *    해당 메서드에서 현재 발생한 이벤트의 status에 따라 다음 sub step을 진행하거나, 이전 sub step으로 롤백하는 보상 트랜잭션을 실행합니다.
     *
     * @param sagaId : SagaState id를 입력합니다.
     * @param eventId : Event id를 입력합니다.
     * @param payload : TransactionInsertEvent 타입의 payload를 입력합니다.
     */
    @Transactional
    public void onTransactionInsertEvent(UUID sagaId, UUID eventId, TransactionInsertEvent payload){
        DepositSaga depositSaga = sagaManager.findDepositSaga(sagaId);

        // 반환된 depositSaga가 null 이면 그대로 return 합니다.
        if (depositSaga == null) {
            return;
        }

        depositSaga.onTransactionInsertEvent(eventId, payload);
    }
}
