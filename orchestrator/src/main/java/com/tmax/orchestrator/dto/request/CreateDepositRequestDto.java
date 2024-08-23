package com.tmax.orchestrator.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDepositRequestDto {

    // 은행 코드
    private String bankCode;

    // 계좌 번호
    private String accountNum;

    // 입금 금액
    private Long depositAmount;


    public ObjectNode toSagaPayload(){
        return new ObjectMapper().createObjectNode()
                .put("bankCode", bankCode)
                .put("accountNum", accountNum)
                .put("depositAmount", depositAmount.toString());
    }
}
