package com.tmax.orchestrator.controller;

import static com.tmax.orchestrator.saga.SagaType.DEPOSIT_CREATE;

import com.tmax.orchestrator.dto.request.CreateDepositRequestDto;
import com.tmax.orchestrator.saga.framework.SagaManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrchestratorController {

    private final SagaManager sagaManager;

    /**
     * 입금 실행 http 요청이 들어오면, sagaManger.beginDepositSaga() 메서드를 호출합니다.
     *
     * @param requestDto : 은행코드, 계좌번호, 입금금액을 입력합니다.
     * @return : 정상적으로 입금 실행이 완료되면 status code 200을 리턴합니다.
     */
    @PostMapping(path = "/deposit")
    ResponseEntity<?> deposit(@RequestBody CreateDepositRequestDto requestDto) {
        // TODO) sagaManager.beginDepositSaga() 메서드를 controller에서 바로 호출하는 것이 맞는지?
        sagaManager.beginDepositSaga(DEPOSIT_CREATE, requestDto.toSagaPayload());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/withdraw")
    ResponseEntity<?> withdraw() {
        return ResponseEntity.accepted()
            .build();
    }

    @PostMapping("/transfer")
    ResponseEntity<?> transfer() {
        return ResponseEntity.accepted()
            .build();
    }
}
