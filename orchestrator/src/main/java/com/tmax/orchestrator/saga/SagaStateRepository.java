package com.tmax.orchestrator.saga;

import com.tmax.orchestrator.domain.SagaState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SagaStateRepository extends JpaRepository<SagaState, UUID> {

}
