package com.tmax.orchestrator.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class EventLog {

    @Id
    private UUID eventId;

    private LocalDateTime issuedOn;

    public EventLog(UUID eventId) {
        this.eventId = eventId;
        this.issuedOn = LocalDateTime.now();
    }
}
