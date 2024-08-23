package com.tmax.orchestrator.event;

import com.tmax.orchestrator.domain.EventLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventLogServiceimpl implements EventLogService {

    private final EventLogRepository eventLogRepository;

    @Override
    public void createEventLog(EventLog eventLog) {
        eventLogRepository.save(eventLog);
    }

    @Override
    public EventLog findEventLog(UUID eventId) {
        return eventLogRepository.findById(eventId).orElse(null);
    }
}
