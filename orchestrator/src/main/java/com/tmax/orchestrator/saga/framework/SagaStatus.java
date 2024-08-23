package com.tmax.orchestrator.saga.framework;

/**
 * SagaState의 현재 상태를 나타내는 Enum 클래스
 */
public enum SagaStatus {

    // 시작됨
    STARTED,

    // 취소중
    ABORTING,

    // 취소됨 (실패)
    ABORTED,

    // 완료됨 (성공)
    COMPLETED;

    /**
     * 현재 SagaStatus가 ABORTED 상태인지 확인하는 메서드.
     *
     * @return : ABORTED 상태인 경우 true, 그 외의 상태인 경우 false를 리턴함.
     */
    public boolean isAborted(){
        return this == ABORTED;
    }

    /**
     * 현재 SagaStatus가 COMPLETED 상태인지 확인하는 메서드.
     *
     * @return : COMPLETED 상태인 경우 true, 그 외의 상태인 경우 false를 리턴함.
     */
    public boolean isCompleted(){
        return this == COMPLETED;
    }
}
