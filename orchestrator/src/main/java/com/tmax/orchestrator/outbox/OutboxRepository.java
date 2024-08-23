package com.tmax.orchestrator.outbox;

import com.tmax.orchestrator.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
}
