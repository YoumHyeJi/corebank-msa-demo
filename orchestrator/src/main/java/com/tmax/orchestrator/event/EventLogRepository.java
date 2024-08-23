package com.tmax.orchestrator.event;

import com.tmax.orchestrator.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
}
