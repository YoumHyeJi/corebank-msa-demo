package com.tmax.orchestrator.saga.framework;

public enum SagaStepStatus {

    // 시작됨
    STARTED,

    // 실패함 (실패)
    FAILED,

    // 성공함 (성공)
    SUCCEEDED,

    // 보상중
    COMPENSATING,

    // 보상됨 (실패)
    COMPENSATED;

    /**
     * 현재 SagaStepStatus가 SUCCEEDED 상태인지 확인하는 메서드.
     *
     * @return : SUCCEEDED 상태인 경우 true, 그 외의 상태인 경우 false를 리턴함.
     */
    public boolean isSucceeded(){
        return this == SUCCEEDED;
    }

    /**
     * 현재 SagaStepStatus가 FAILED 혹은 COMPENSATED 상태인지 확인하는 메서드.
     *
     * @return : FAILED 혹은 COMPENSATED 상태인 경우 true, 그 외의 상태인 경우 false를 리턴함.
     */
    public boolean isFailedOrCompensated(){
        return this == FAILED || this == COMPENSATED;
    }

}
