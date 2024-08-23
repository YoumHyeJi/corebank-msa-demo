package com.tmax.orchestrator.event;

import com.tmax.orchestrator.domain.EventLog;

import java.util.UUID;

public interface EventLogService {

    void createEventLog(EventLog eventLog);

    EventLog findEventLog(UUID eventId);
}
