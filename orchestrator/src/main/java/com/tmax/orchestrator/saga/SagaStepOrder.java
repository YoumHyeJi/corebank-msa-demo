package com.tmax.orchestrator.saga;

public interface SagaStepOrder<T extends SagaStepOrder> {

    /**
     * DepositSaga의 다음 sub step을 리턴하는 메서드
     * @return : 다음 DepositSagaStepOrder
     */
    T nextStep();

    /**
     * DepositSaga의 이전 sub step을 리턴하는 메서드
     * @return : 이전 DepositSagaStepOrder
     */
    T previousStep();

}
