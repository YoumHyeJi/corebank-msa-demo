package org.tmax.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @Column(name = "id", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 36)
    private String name;

    @Column(name = "bank_code")
    private String BankCode;

    @Column(name = "account_num")
    private String AccountNum;

    @Column(name = "balance")
    private Long Balance;

    @Column(name = "withdrawal_limit")
    private Long WithdrawalLimit;

    @Column(name = "transfer_limit")
    private Long TransferLimit;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "status")
    private String status;
}
