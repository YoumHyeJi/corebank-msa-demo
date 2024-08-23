package org.tmax.account.domain;

import jakarta.persistence.*;
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
public class TransferTx {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long amount;

    private UUID depositTxId;

    private UUID withdrawalTxId;

    private LocalDateTime transactionDateTime;

    private String status;
}
