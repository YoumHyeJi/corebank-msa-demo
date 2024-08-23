package com.tmax.orchestrator.saga;


import com.tmax.orchestrator.domain.SagaState;

import java.util.UUID;

public interface SagaStateService {

    void createSagaState(SagaState sagaState);

    SagaState findSagaState(UUID sagaId);
}
