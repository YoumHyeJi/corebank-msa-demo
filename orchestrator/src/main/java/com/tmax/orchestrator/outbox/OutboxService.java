package com.tmax.orchestrator.outbox;

import com.tmax.orchestrator.domain.Outbox;

public interface OutboxService {

    void createOutbox(Outbox outbox);
}
