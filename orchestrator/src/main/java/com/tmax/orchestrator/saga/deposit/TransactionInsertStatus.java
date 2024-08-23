package com.tmax.orchestrator.saga.deposit;


import com.tmax.orchestrator.saga.framework.SagaStepStatus;

public enum TransactionInsertStatus {
    REQUESTED, CANCELLED, FAILED, COMPLETED;

    public SagaStepStatus toSagaStepStatus() {
        return switch (this) {
            case CANCELLED -> SagaStepStatus.COMPENSATED;
            case COMPLETED, REQUESTED -> SagaStepStatus.SUCCEEDED;
            case FAILED -> SagaStepStatus.FAILED;
        };
    }
}
