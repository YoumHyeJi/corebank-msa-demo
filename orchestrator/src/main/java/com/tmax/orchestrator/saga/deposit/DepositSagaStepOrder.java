package com.tmax.orchestrator.saga.deposit;


import com.tmax.orchestrator.saga.SagaStepOrder;

/**
 * DepositSaga의 Sub Step Order를 정의하는 enum 클래스입니다.
 * 각 Sub Step의 이전 및 다음 Sub Step을 함께 정의합니다.
 */
public enum DepositSagaStepOrder implements SagaStepOrder<DepositSagaStepOrder> {

    /**
     * 계좌 잔액을 업데이트 하는 Sub Step
     */
    ACCOUNT_BALANCE_UPDATE {
        /**
         * ACCOUNT_BALANCE_UPDATE 의 다음 서브 스탭은 TRANSACTION_INSERT 입니다.
         */
        @Override
        public DepositSagaStepOrder nextStep() {
            return TRANSACTION_INSERT;
        }

        /**
         * ACCOUNT_BALANCE_UPDATE 의 이전 서브 스탭은 없습니다.
         */
        @Override
        public DepositSagaStepOrder previousStep() {
            return null;
        }
    },

    /**
     * 입출금 트랜잭션에 데이터를 저장하는 Sub Step
     */
    TRANSACTION_INSERT {
        /**
         * TRANSACTION_INSERT 의 다음 서브 스탭은 없습니다.
         */
        @Override
        public DepositSagaStepOrder nextStep() {
            return null;
        }

        /**
         * TRANSACTION_INSERT 의 이전 서브 스탭은 ACCOUNT_BALANCE_UPDATE 입니다.
         */
        @Override
        public DepositSagaStepOrder previousStep() {
            return ACCOUNT_BALANCE_UPDATE;
        }
    };

    /**
     * DepositSaga의 처음 Sub Step을 리턴하는 메서드입니다.
     *
     * @return : ACCOUNT_BALANCE_UPDATE 서브 스탭
     */
    public static DepositSagaStepOrder startStep() {
        return ACCOUNT_BALANCE_UPDATE;
    }

    /*
    public static DepositSagaStepOrder startStep() {
        return ACCOUNT_BALANCE_UPDATE;
    }

    public abstract DepositSagaStepOrder nextStep();


    public abstract DepositSagaStepOrder previousStep();*/
}
