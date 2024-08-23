package com.tmax.orchestrator.saga;

import com.tmax.orchestrator.domain.SagaState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SagaStateServiceImpl implements SagaStateService {

    private final SagaStateRepository sagaStateRepository;

    /**
     * SagaState 엔티티를 DB에 저장하는 메서드입니다.
     *
     * @param sagaState : DB에 저장할 SagaState 엔티티를 입력합니다.
     */
    @Override
    public void createSagaState(SagaState sagaState){
        sagaStateRepository.save(sagaState);
    }

    /**
     * SagaState 엔티티를 DB에서 조회하는 메서드입니다.
     *
     * @param sagaId : SagaState id를 입력합니다.
     * @return : DB에서 조회한 SagaState 엔티티를 반환합니다.
     */
    @Override
    public SagaState findSagaState(UUID sagaId) {
        return sagaStateRepository.findById(sagaId).orElse(null);
    }
}
